package com.draculavenom.notification.utilities;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.whatsappConfig.model.WhatsappConfig;

@Service
public class WhatsappService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(WhatsappConfig config, String phone, String message) {
        String url = "https://graph.facebook.com/v19.0/" + config.getPhoneNumberId() + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", phone);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);
        body.put("text", text);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(url, request, String.class);

    }

    public void sendMainMenu(WhatsappConfig config, String phone){
        String body = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "interactive",
            "interactive": {
                "type": "button",
                "body": {
                    "text": "What do you want to do?"
                },
                "action": {
                    "buttons": [
                    {
                        "type": "reply",
                        "reply": {
                            "id": "BOOK",
                            "title": "Schedule appointment"
                        }
                    },
                    {
                        "type": "reply",
                        "reply": {
                            "id": "CANCEL",
                            "title": "Cancel appointment"
                        }
                    }
                    ]
                }    
            }
        }
                """.formatted(phone);
        sendRaw(config, body);
    }

    public void sendRaw(WhatsappConfig config, String jsonBody){
        String url = "https://graph.facebook.com/v19.0/" + config.getPhoneNumberId() + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        restTemplate.postForObject(url, request, String.class);
    }

    public String formatAppointment(Appointment ap){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM", Locale.ENGLISH);
        String date = ap.getDate().format(formatter);
        String time = ap.getTime().toString().substring(0,5);

        return date + " - " + time;
    }

    public void sendAppointmentList(WhatsappConfig config, String phone, List<Appointment> appointments, int page){
        int pageSize = 8;
        int start = page * pageSize;
        int end = Math.min(start + pageSize, appointments.size());

        StringBuilder rows = new StringBuilder();

        for(int i = start; i < end; i++){
            Appointment ap = appointments.get(i);
            rows.append("""
                {
                    "id": "%s",
                    "title": "%s"
                },
                """.formatted(ap.getId(), formatAppointment(ap)));
        }

        if(page > 0){
            rows.append("""
                {
                    "id": "PREV_PAGE_%d",
                    "title": "Previous"   
                },
            """.formatted(page - 1));
        }

        if(end < appointments.size()){
            rows.append("""
                {
                    "id": "NEXT_PAGE_%d",
                    "title": "Next"
                },
            """.formatted(page + 1));
        }

        String rowsFinal = rows.substring(0, rows.length() - 1);

        String body = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "interactive",
            "interactive": {
                "type": "list",
                "body": {
                    "text": "Select appointment to cancel"
                },
                "action": {
                    "button": "View",
                    "sections": [
                        {
                            "title": "Your appointments",
                            "rows": [
                                %s
                            ]
                        }                    
                    ]
                }
            }
        }    
        """.formatted(phone, rowsFinal);
        sendRaw(config, body);
    }

    public void sendConfirmationButtons(WhatsappConfig config, String phone){
        String body = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "interactive",
            "interactive": {
                "type": "button",
                "body": {
                    "text": "Are you sure you want to cancel this appointment?"
                },
                "action": {
                    "buttons": [
                    {
                        "type": "reply",
                        "reply": {
                            "id": "CONFIRM_CANCEL",
                            "title": "Yes, cancel"
                        }
                    },
                    {
                        "type": "reply",
                        "reply": {
                            "id": "KEEP_APPOINTMENT",
                            "title": "No"
                        }
                    }
                    ]
                }    
            }
        }        
        """.formatted(phone);
        sendRaw(config, body);
    }

    public void sendSuggestedSlot(WhatsappConfig config, String phone, LocalDateTime slot){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM", Locale.ENGLISH);
        String date = slot.toLocalDate().format(formatter);
        String time = slot.toLocalTime().toString().substring(0,5);
        String body = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "interactive",
            "interactive": {
                "type": "button",
                "body": {
                    "text": "Next available appointment:\\n%s %s\\n\\nDo you want to book it?"
                },
                "action": {
                    "buttons": [
                    {
                        "type": "reply",
                        "reply": {
                            "id": "ACCEPT_SLOT",
                            "title": "Accept"
                        }
                    },
                    {
                        "type": "reply",
                        "reply": {
                            "id": "CHANGE_SLOT",
                            "title": "Choose another"
                        }
                    }
                    ]
                }    
            }
        }
                """.formatted(phone, date, time);
        sendRaw(config, body);
    }

    public void sendAvailableSlots(WhatsappConfig config, String phone, List<LocalTime> slots, int page){
        int pageSize = 8;
        int totalPages = (slots.size() + pageSize - 1) / pageSize;
        int start = page * pageSize;
        int end = Math.min(start + pageSize, slots.size());
        if(start >= slots.size()){
            return;
        }
        List<LocalTime> pageSlots = slots.subList(start, end);

        StringBuilder rows = new StringBuilder();
        for(LocalTime t : pageSlots){
            String formatted = t.toString().substring(0,5);
            rows.append("""
                {
                    "id": "%s",
                    "title": "%s"
                },
            """.formatted(formatted, formatted));
        }

        if(page > 0){
            rows.append("""
                {
                    "id": "PREV_PAGE_%d",
                    "title": "Previous page"
                },
            """.formatted(page - 1));
        }

        if(page < totalPages - 1){
            rows.append("""
                {
                    "id": "NEXT_PAGE_%d",
                    "title": "Next page"
                },
            """.formatted(page + 1));
        }

        String rowsFinal = rows.substring(0, rows.length() -1);

        String body = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "interactive",
            "interactive": {
                "type": "list",
                "body": {
                    "text": "Select a time"
                },
                "action": {
                    "button": "Available times",
                    "sections": [
                        {
                            "title": "Slots",
                            "rows": [
                                %s
                            ]
                        }
                    ]
                }
            }
        }
        """.formatted(phone, rowsFinal);

        sendRaw(config, body);
    }

}

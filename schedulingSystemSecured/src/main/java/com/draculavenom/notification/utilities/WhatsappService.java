package com.draculavenom.notification.utilities;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.draculavenom.schedulingSystem.model.Appointment;

@Service
public class WhatsappService {
    @Value("${whatsapp.token}")
    private String token;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String phone, String message) {
        String url = "https://graph.facebook.com/v19.0/" + phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
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

    public void sendMainMenu(String phone){
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
        sendRaw(body);
    }

    public void sendRaw(String jsonBody){
        String url = "https://graph.facebook.com/v19.0/" + phoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
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

    public void sendAppointmentList(String phone, List<Appointment> appointments){
        StringBuilder rows = new StringBuilder();

        for(Appointment ap : appointments){
            rows.append("""
                {
                    "id": "%s",
                    "title": "%s"
                },
                """.formatted(ap.getId(), formatAppointment(ap)));
        }

        String rowsFinal = rows.length() > 0 ? rows.substring(0, rows.length() - 1) : "";

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
        sendRaw(body);
    }
}

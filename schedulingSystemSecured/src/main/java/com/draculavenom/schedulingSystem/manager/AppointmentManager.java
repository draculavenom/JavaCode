package com.draculavenom.schedulingSystem.manager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.draculavenom.notification.service.NotificationService;
import com.draculavenom.schedulingSystem.dto.AppointmentResponseDTO;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AppointmentManager {

	@Autowired private AppointmentRepository repository;
	@Autowired private UserRepository userRepository;
	@Autowired private NotificationService notificationService;

	private Appointment applyStatusChange(Appointment appointment, AppointmentStatus newStatus, String comment, boolean systemAction){
		boolean requiresComment = false;
		if(newStatus == AppointmentStatus.CANCELLED && !systemAction){
			requiresComment = true;
		}

		if(requiresComment) {
			if(comment == null || comment.trim().isEmpty()) {
				throw new IllegalArgumentException("Comment is requires for this action.");
			}
			appointment.setComment(comment);
		} else {
			if(comment != null && !comment.trim().isEmpty()) {
				appointment.setComment(comment);
			}
		}
		appointment.setStatus(newStatus);

		return repository.save(appointment);
	}

	private Appointment cancelInternal(Appointment appointment, String comment, boolean systemAction) {
		if(!systemAction) {
			if(comment == null || comment.trim().isEmpty()) {
				throw new IllegalArgumentException("Comment is required when cancelling");
			}
			appointment.setComment(comment);
		} else {
			appointment.setComment("System cancelled appointment due to expiration.");
		}

		appointment.setStatus(AppointmentStatus.CANCELLED);
		return repository.save(appointment);
	}
	
	public Appointment create(Appointment ap) throws Exception{
		User user = userRepository.findById(ap.getUserId()).orElseThrow();
		User manager = userRepository.findById(ap.getManager().getId()).orElseThrow();
		ap.setManager(manager);

		if(!manager.getCompany().getId().equals(user.getCompany().getId())){
			throw new Exception("Manager does not belong to your company");
		}

		List<Appointment> appointments = getAppointmentsByManagerWithStatus(manager.getId());

		if(appointments.stream().filter(a -> a.getDate().equals(ap.getDate()) && a.getTime().equals(ap.getTime())).findFirst().isEmpty()) {
			if((ap.getDate().isBefore(LocalDate.now())) || (ap.getDate().equals(LocalDate.now()) && ap.getTime().isBefore(LocalTime.now()))) {
				throw new Exception("You can't schedule appointments in the past. Please select a valid option");
			} else {
				Appointment saved = repository.save(ap);
				try {
					notificationService.notifyAppointmentCreated(saved);
				} catch (Exception e) {
					log.error("Error sending notification", e);
				}
				return saved;

			}
		}else
			throw new Exception("That slot is already been scheduled, please try again with a different slot");
	}

	public AppointmentResponseDTO toDTO(Appointment ap){
		AppointmentResponseDTO dto = new AppointmentResponseDTO();
		dto.setAppointmentId(ap.getId());
		dto.setDate(ap.getDate());
		dto.setTime(ap.getTime());
		dto.setStatus(ap.getStatus());
		dto.setUserId(ap.getUserId());
		dto.setComment(ap.getComment());
		if(ap.getManager() != null){
			dto.setManagerId(ap.getManager().getId());
		}
		return dto;
	}
	
	public Appointment update(Appointment ap) throws Exception{
		User user = userRepository.findById(ap.getUserId()).orElseThrow();
		User manager = userRepository.findById(ap.getManager().getId()).orElseThrow();
		if(!manager.getCompany().getId().equals(user.getCompany().getId())){
			throw new Exception("Manager does not belong to your company");
		}
		List<Appointment> appointments = getAppointmentsByManagerWithStatus(manager.getId());
		if(appointments.stream().filter(a -> a.getDate().equals(ap.getDate()) && a.getTime().equals(ap.getTime())).findFirst().isEmpty()) {
			if((ap.getDate().isBefore(LocalDate.now())) || (ap.getDate().equals(LocalDate.now()) && ap.getTime().isBefore(LocalTime.now())))
				throw new Exception("You can't schedule appointments in the past. Please select a valid option");
			else{
				Appointment a = repository.findById(ap.getId()).orElseThrow(Exception::new);
				boolean dateChanged = !a.getDate().equals(ap.getDate()) || !a.getTime().equals(ap.getTime());
				a.setDate(ap.getDate());
				a.setStatus(ap.getStatus());
				a.setTime(ap.getTime());

				if(dateChanged) {
					if(ap.getComment() == null || ap.getComment().trim().isEmpty()) {
						throw new IllegalArgumentException("Comment is required when rescheduling.");
					}
					a.setComment(ap.getComment());
				}
				return repository.save(a);
			}
		}else
			throw new Exception("That slot is already been scheduled, please try again with a different slot");
	}
	
	public Appointment getAppointmentAndCancelIt(Integer id, String comment) {
		Appointment ap = repository.findById(id).orElseThrow();
		return cancelAppointment(ap, comment);
	}
	
	public Appointment cancelAppointment(Appointment ap, String comment) {
		//ap.setStatus(AppointmentStatus.CANCELLED);
		//repository.save(ap);
		//return ap;
		return cancelInternal(ap, comment, false);
	}

	public Appointment CancelBySystem(Appointment appointment) {
		return cancelInternal(appointment, null, true);
	}
	
	public List<Appointment> getAppointmentsManagedByUserId(int userId){
		User user = userRepository.findById(userId).orElseThrow();
		List<AppointmentStatus> appointmentStatuses = new ArrayList<AppointmentStatus>();
		appointmentStatuses.add(AppointmentStatus.SCHEDULED);
		appointmentStatuses.add(AppointmentStatus.COMPLETED);
		appointmentStatuses.add(AppointmentStatus.CONFIRMED);
		List<Appointment> appointments = repository.findAllByStatusIn(appointmentStatuses);
		if(user.getManagedBy() != null) {
			List<User> users = userRepository.findAllByManagedBy(user.getManagedBy()).orElseThrow();
			appointments = appointments.stream().filter(a -> users.stream().map(u -> u.getId()).collect(Collectors.toList()).contains(a.getUserId())).collect(Collectors.toList());			
		}
		return appointments;
	}

	public List<Appointment> getAppointmentsByManagerWithStatus(Integer managerId){
		List<AppointmentStatus> statuses = List.of(
			AppointmentStatus.SCHEDULED,
			AppointmentStatus.COMPLETED,
			AppointmentStatus.CONFIRMED
		);
		return repository.findByManagerIdAndStatusIn(managerId, statuses);
	}
	
	public List<Appointment> getAppointmentsManagedByUserIdAllStatus(int userId){
		List<Appointment> appointments = repository.findAll();
		List<User> users = userRepository.findAllByManagedBy(userId).orElseThrow();
		appointments = appointments.stream().filter(a -> users.stream().map(u -> u.getId()).collect(Collectors.toList()).contains(a.getUserId())).collect(Collectors.toList());			
		return appointments;
	}
	
	public Appointment updateStatus(int id, AppointmentStatus status, String comment) {
		Appointment appointment = repository.findById(id).orElseThrow();
		Appointment saved = applyStatusChange(appointment, status, comment, false);
		try{
			notificationService.notifyAppointmentStatusChanged(saved, status);
		}catch(Exception e){
			log.error("Error sending appointment status notification", e);
		}
		return saved;
	}

	public List<Appointment> getAppointmentsByCompany(Integer userId){
		User user = userRepository.findById(userId).orElseThrow();
		return repository.findByManagerCompany(user.getCompany());
	}

	public List<Appointment> getAppointmentsByManagerAndCompany(Integer managerId, Integer userId){
		User user = userRepository.findById(userId).orElseThrow();
		return repository.findByManagerIdAndManagerCompany(managerId, user.getCompany());
	}

	public List<AppointmentResponseDTO> toDTOList(List<Appointment> list){
		return list.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}
}

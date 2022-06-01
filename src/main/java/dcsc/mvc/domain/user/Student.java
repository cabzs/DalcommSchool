package dcsc.mvc.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	@Id
	private String studentId;
	
	private String studentPwd;
	
	private String studentName;
	
	private String studentPhone;
	
	private String studentEmail;
	
	@CreationTimestamp
	private LocalDateTime studentInsertDate;
	
	private String studentQuit;
	
	
}

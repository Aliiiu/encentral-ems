package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.EventStatus;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@Table(name="event")
public class JpaEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="event_id", unique=true, nullable=false, length=64)
	private String eventId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Temporal(TemporalType.DATE)
	@Column(name="end_date", nullable=false)
	private Date endDate;

	@Column(name="event_description", nullable=false, length=100)
	private String eventDescription;

	@Column(name="event_status", nullable=false, length=2147483647)
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;

	@Column(name="event_title", nullable=false, length=64)
	private String eventTitle;

	@Column(name="modified_by")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date", nullable=false)
	private Date startDate;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value", nullable=false)
	private JpaOption eventType;

	public JpaEvent() {
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEventDescription() {
		return this.eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventTitle() {
		return this.eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public JpaOption getEventType() {
		return eventType;
	}

	public void setEventType(JpaOption eventType) {
		this.eventType = eventType;
	}

	@Override
	public int hashCode() {
		if (this.getEventId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getEventId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaEvent event = (JpaEvent) obj;
		return Objects.equals(this.getEventId(), event.getEventId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Title", this.getEventTitle())
				.add("EventType", this.getEventType())
				.add("Date", this.getStartDate())
				.add("Status", this.getEventStatus())
				.toString();
	}
}
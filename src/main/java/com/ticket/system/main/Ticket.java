package com.ticket.system.main;


/**
 * Ticket class represents a single ticket for an event.
 * It contains details such as the ticket ID, event name, description, date, location, and price.
 */
public class Ticket {

    private int ticketId;
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventLocation;
    private int ticketPrice;

    /**
     * Constructor to initialize the Ticket object with specific values.
     *
     * @param ticketId         Unique identifier for the ticket
     * @param eventName        Name of the event
     * @param eventLocation    Location of the event
     * @param ticketPrice      Price of the ticket
     * @param eventDate        Date of the event
     * @param eventDescription Description of the event
     */
    public Ticket(int ticketId, String eventName, String eventLocation, int ticketPrice, String eventDate, String eventDescription) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.ticketPrice = ticketPrice;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
    }

    //Getters and Setters
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}


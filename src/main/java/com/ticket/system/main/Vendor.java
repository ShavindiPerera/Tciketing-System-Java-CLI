package com.ticket.system.main;

/**
 * Vendor class implements the Runnable interface to simulate a ticket vendor releasing tickets to the ticket pool.
 * Each vendor is responsible for adding a specified number of tickets at a defined release rate.
 */
public class Vendor implements Runnable{

    private TicketPool ticketPool;
    private int totalTicketsReleased;
    private int tReleaseRate;



    /**
     * Constructor for the Vendor class.
     *
     * @param ticketPool          Shared ticket pool to which tickets will be added
     * @param totalTicketsReleased Total number of tickets this vendor will release
     * @param tReleaseRate         Time interval (in seconds) between releasing tickets
     */
    public Vendor( TicketPool ticketPool, int totalTicketsReleased, int tReleaseRate) {
        this.ticketPool = ticketPool;
        this.totalTicketsReleased = totalTicketsReleased;
        this.tReleaseRate = tReleaseRate;

    }

    /**
     * Run method to execute the ticket releasing process.
     * Each vendor adds tickets to the pool at the specified release rate until all tickets are released.
     */
    @Override
    public void run() {
        for (int i = 1; i <= totalTicketsReleased; i++ ){

           Ticket ticket = new Ticket(1, "Show","Colombo",1000, "25th of December","Musical");


            ticketPool.addTickets(ticket);
            try {
                Thread.sleep(tReleaseRate * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}

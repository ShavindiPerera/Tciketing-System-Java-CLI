package com.ticket.system.main;


/**
 * Customer class implements the Runnable interface to simulate a customer retrieving tickets from the ticket pool.
 * Each customer attempts to retrieve a specified quantity of tickets at a defined retrieval rate.
 */
public class Customer implements Runnable{
    private TicketPool ticketPool;
    private int cRetrievalRate;
    private int quantity;


    /**
     * Constructor for the Customer class.
     *
     * @param ticketPool    Shared ticket pool from which tickets will be retrieved
     * @param cRetrievalRate Time interval (in seconds) between ticket retrievals
     * @param quantity       Total number of tickets the customer will attempt to retrieve
     */
    public Customer(TicketPool ticketPool, int cRetrievalRate, int quantity) {
        this.ticketPool = ticketPool;
        this.cRetrievalRate = cRetrievalRate;
        this.quantity = quantity;
    }



    /**
     * Run method to execute the ticket retrieval process.
     * The customer attempts to retrieve tickets from the pool at the specified retrieval rate until the desired quantity is reached.
     */
    @Override
    public void run() {
        for(int i = 1; i <quantity; i++){
         Ticket ticket = ticketPool.removeTicket();

            try {
                Thread.sleep(cRetrievalRate * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

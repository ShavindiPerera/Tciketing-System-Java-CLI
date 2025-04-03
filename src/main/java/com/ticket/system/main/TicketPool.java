package com.ticket.system.main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class TicketPool {
    private ConcurrentLinkedQueue<Ticket> ticketQueue;
    private int maximumCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    /**
     * Constructs a TicketPool with a specified maximum capacity.
     * Initializes a ConcurrentLinkedQueue to manage available tickets efficiently.
     *
     * @param maximumCapacity the maximum number of tickets that can be held in the pool
     */
    public TicketPool(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        this.ticketQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Adds a ticket to the ticket pool, ensuring the pool does not exceed its maximum capacity.
     * If the pool is full, the method waits until space becomes available. Once a ticket is added,
     * all threads waiting for a ticket are notified.
     *
     * @param ticket the ticket to be added to the pool
     * @throws RuntimeException if the thread is interrupted while waiting or adding a ticket
     */
    public void addTickets(Ticket ticket) {
        lock.lock();
        try {
            while (ticketQueue.size() >= maximumCapacity) {
                notFull.await(); // Wait until space is available
            }

            ticketQueue.add(ticket);
            System.out.println(Thread.currentThread().getName() + " released a ticket. Currently available tickets: " + ticketQueue.size());
            notEmpty.signalAll(); // Notify threads waiting for a ticket
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes and returns a ticket from the ticket pool.
     * If the pool is empty, the method waits until a ticket becomes available. Once a ticket is removed,
     * all threads waiting for space are notified.
     *
     * @return the ticket removed from the pool
     * @throws RuntimeException if the thread is interrupted while waiting or removing a ticket
     */
    public Ticket removeTicket() {
        lock.lock();
        try {
            while (ticketQueue.isEmpty()) {
                notEmpty.await(); // Wait until a ticket is available
            }

            Ticket ticket = ticketQueue.poll();
            System.out.println(Thread.currentThread().getName() + " has bought a ticket. Currently available tickets: " + ticketQueue.size());
            notFull.signalAll(); // Notify threads waiting for space
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}


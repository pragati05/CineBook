# UI mockups

Static HTML, no build step. Open any file in a browser, or start with [home](home.html). Shared styles are in `mockups.css`. Names, times and figures are example data.

- [Home](home.html): Movie grid for the selected city, with filters. The city picker is the entry point to multi-city support.
- [Movie and showtimes](movie.html): Date strip, then showtimes per theatre. Colour and wording both signal availability: open, filling fast, sold out.
- [Seat picker](seats.html): Hold countdown, tiered pricing and an explicit line saying nothing is charged now. Booked seats are hatched, so state does not depend on colour alone.
- [Booking confirmation and ticket](ticket.html): Amount payable at the counter, booking reference and the signed QR for the gate.
- [My bookings](bookings.html): Lifecycle states from the booking state machine: confirmed, needs attendance confirmation, attended, released.
- [Admin dashboard](admin.html): Occupancy plus a no-show risk column from insights-service, with the reason shown so staff can judge it.
- [Seat picker (mobile)](seats-m.html): Same flow at 360 px.
- [Ticket (mobile)](ticket-m.html): The screen most people will open at the gate.
- [Staff scanner (mobile)](checkin-m.html): Camera view, large pass/fail result, and the reuse message from the atomic check-in.

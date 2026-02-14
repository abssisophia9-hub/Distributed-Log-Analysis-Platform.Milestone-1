# Milestone-1
Implementing a Distributed Log Analysis Platform (DLAP) for analyzing web server logs in the Common Log Format (CLF)

Dlap Protocol 
The client opens a connection with the server and informs the server whether it wants to count or code distribution.

count
If the client wants to count, then the header will be as the following:
Functionality 1: Request Count
    ◦ Request Header: COUNT [filename]\n
    ◦ Server Response Header: OK [count]\n or ERROR [reason]\n
• Functionality 2: Status Code Distribution
    ◦ Request Header: DIST [filename]\n
    ◦ Server Response Header: OK [bodySize]\n (The bodySize metadata allows the client to "allocate space in memory" before reading the response [5, 56, 58 image]).
    ◦ Server Response Body: Raw bytes containing the distribution results.
    ◦ Error Response: ERROR [reason]\n.

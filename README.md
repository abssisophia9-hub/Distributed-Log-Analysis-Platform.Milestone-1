# Milestone-1
Implementing a Distributed Log Analysis Platform (DLAP) for analyzing web server logs in the Common Log Format (CLF)

## Design: Dlap Protocol 
Once the connection is established, the client informs the server whether it wants to perform a request count or a status code distribution analysis using a header.
Functionality 1: Request Count If the client wants to count the total requests in a log file, the header will be as follows:
• COUNT[one space][file name][Line Feed]

Upon receiving this header, the server searches for the specified file and performs the analysis.
• If the file is not found or cannot be parsed, the server shall reply with:
    ◦ ERROR[one space][reason][Line Feed] [54, 58 image]
• Else, the server shall reply with a header containing the result:
    ◦ OK[one space][count][Line Feed]
Functionality 2: Status Code Distribution If the client wants the distribution of HTTP status codes, the header will be as follows:
• DIST[one space][file name][Line Feed]

Upon receiving this header, the server performs the analysis.
• If the analysis is successful, the server shall reply with a header containing metadata (body size) followed by the bytes of the distribution data:
    ◦ OK[one space][body size][Line Feed]
    ◦ [Bytes of the distribution data]
• Else, it shall send an error message as follows:
    ◦ ERROR[one space][reason][Line Feed] [58 image]


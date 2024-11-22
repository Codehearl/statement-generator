
# **Statement Generation Application**  
A robust, scalable system for generating client financial statements efficiently and securely. Designed for enterprise use, this application facilitates seamless user authentication, statement generation, and file management in both single and bulk operations.

---

## **Features**  
- **User Authentication**  
  - Secure login via Active Directory (LDAP).  
  - JWT-based session management for secure operations.

- **Statement Generation**  
  - Generate financial statements for single or multiple customer PINs.  
  - Bulk processing using CSV uploads.

- **File Handling**  
  - Save generated PDFs to a designated network location.  
  - Option to download statements as individual PDFs or a compressed ZIP file.

- **Error Handling and Logging**  
  - Frontend input validation to reduce errors.  
  - Backend logging for activity and error tracking.

- **Interactive API Documentation**  
  - Swagger/OpenAPI integration for developer-friendly documentation.  

---

## **Technology Stack**  

### **Backend**  
- **Language**: Java  
- **Framework**: Spring Boot  
- **Authentication**: Active Directory (LDAP), JWT  
- **PDF Generation**: Apache PDFBox / iText  
- **Logging**: Java Util Logging  

### **Frontend**  
- **Languages**: HTML, CSS, JavaScript  
- **Libraries**: jQuery, SweetAlert2  

### **Other Tools**  
- **API Documentation**: Swagger (springdoc-openapi)  
- **Version Control**: GitHub  
- **Build Tools**: Maven  
- **Deployment**: Payara Server (On-Premises)  

---

## **Setup Instructions**  

### **Prerequisites**  
1. Java Development Kit (JDK) 17 or later.  
2. Maven for dependency management.  
3. Payara Server 6 installed and configured.  
4. Active Directory credentials for LDAP setup.  
5. Network storage access for file handling.  

### **Installation**  
1. **Clone the Repository**  
   ```bash
   git clone https://github.com/codehearl/statement-generator.git
   cd statement-generator
   ```

2. **Configure Properties**  
   - Update the `application.properties` file with your environment-specific settings:  
     ```properties
     server.port=8080
     ldap.url=ldap://your-ldap-server
     ldap.base-dn=dc=example,dc=com
     jwt.secret=your-secret-key
     file.storage.path=/network/storage/path
     ```

3. **Build the Application**  
   ```bash
   mvn clean install
   ```

4. **Deploy to Payara Server**  
   - Copy the `.war` file from the `target` directory to the Payara deployment folder.  

5. **Start the Server**  
   - Start Payara Server and verify the application is running at `http://localhost:8080`.  

---

## **Usage**  

### **User Login**  
1. Navigate to the login page at `/login`.  
2. Enter LDAP credentials.  

### **Single Statement Generation**  
1. Access the form at `/generate-single`.  
2. Enter the customer PIN and other required details.  
3. Click "Generate" to download the PDF.  

### **Bulk Statement Generation**  
1. Upload a CSV file containing customer PINs at `/generate-bulk`.  
2. Download the ZIP file containing all generated PDFs.  

---

## **API Endpoints**  

### **Authentication**  
- **POST** `/auth/login`  
  Validates credentials and returns a JWT token.  

### **Statement Generation**  
- **POST** `/statements/single`  
  Generate a statement for a single customer PIN.  

- **POST** `/statements/bulk`  
  Upload a CSV file for bulk statement generation.  

---

## **Testing**  

### **Unit Testing**  
Run unit tests using JUnit and Mockito:  
```bash
mvn test
```

### **Integration Testing**  
Execute integration tests using Spring Boot Test:  
```bash
mvn verify
```

### **Functional Testing**  
- Use Postman or equivalent tools to test API endpoints.  
- Use Selenium for end-to-end UI testing.  

---


## **License**  
This project is licensed under the [MIT License](LICENSE).

---

## **Contact**  
For support or inquiries, reach out to:  
- **Email**: a-sanusi@leadway-pensure.com   


# Software Design Project - Team 12

## Structure
1. **client** - a simple client built using JavaFX and OCSF. We use EventBus (which implements the mediator pattern) in order to pass events between classes (in this case: between SimpleClient and PrimaryController).
2. **server** - a simple server built using OCSF.
3. **entities** - a shared module where all the entities of the project live.

## Running
1. Run Maven install **in the parent project**.
2. Run the server using the `exec:java` goal in the server module.
3. Run the client using the `javafx:run` goal in the client module.

## Examples
Movie Catalog:
![Screenshot 2025-03-09 212755](https://github.com/user-attachments/assets/2894c5f6-a767-4946-a63a-a7dbe346f4dd)

Movie Description:
![image](https://github.com/user-attachments/assets/fd3f4370-c06f-4562-9aea-18f0c15c9e5d)

Seat Selection:
![image](https://github.com/user-attachments/assets/c877afbf-02da-4cbd-ad57-9c9e7040e38c)

Purchase screen:
![image](https://github.com/user-attachments/assets/df1561b8-eaa8-4c1e-81ea-3db5d0133a00)

Customer Panel:
![Screenshot 2025-03-09 213649](https://github.com/user-attachments/assets/af2b05d6-462b-43e2-98e7-9980107b3b8b)

![image](https://github.com/user-attachments/assets/37219499-d4ea-4bf5-b25b-e6515d012e3a)

Employee Panel (Log-in):
![image](https://github.com/user-attachments/assets/c33135d4-7289-49bb-8a73-fcf201f00798)

Employee Panel (Service Employee):
![image](https://github.com/user-attachments/assets/d9c5945c-b70f-4eb8-8f0b-73f891beec36)

![image](https://github.com/user-attachments/assets/c584589e-0688-49fc-a10f-60201f846bd8)

Complaint Submission:
![image](https://github.com/user-attachments/assets/b87bbfce-4e75-457d-b9f3-2c796ba200ed)

Administrator Panel:
![image](https://github.com/user-attachments/assets/03d6097e-87b9-4fc2-a804-fe2ad789511f)



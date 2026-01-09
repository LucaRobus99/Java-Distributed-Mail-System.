

# 📧 Java Distributed Mail System

## 🏛 Project Overview

**Official Description of project:** *Development of a Java application implementing an electronic mail service, organized with a mail server managing user mailboxes and the mail clients necessary for users to access their respective accounts.*

This project was developed for the **Programming III (Programmazione 3)** course at the **University of Turin (Università degli Studi di Torino)**.

## 🎯 Educational Objectives

Beyond implementing a functional email system, this project serves as a comprehensive laboratory for mastering advanced Java concepts:

* **Concurrent Programming**: Implementation of a multi-threaded server capable of handling simultaneous TCP connections using Java **Threads**.
* **JavaFX GUI**: Building a responsive and modern user interface using the JavaFX framework.
* **Observable Properties**: Extensive use of **JavaFX Properties** and **Data Binding** to ensure the UI automatically reflects the underlying data model state (the "Model-View-Controller" or MVC pattern).
* **Network Programming**: Managing socket-based communication and object serialization between distributed components.

---

## ✨ Features

* **Client-Server Architecture**: A multi-threaded server handles requests from multiple clients simultaneously without blocking.

* **Persistent Storage**: User mailboxes are persisted as **XML files**, ensuring data is saved even after the server restarts.
* **Advanced UI Binding**: The client uses JavaFX Properties to synchronize the inbox view with the server's real-time state.
* **User Authentication**: A login system that validates predefined email addresses and prevents multiple simultaneous logins to the same account.
* **Core Email Functionality**:
* **Compose & Send**: Multi-recipient support.
* **Receive & View**: Dynamic display of selected email content.
* **Management**: Delete, Reply, Reply All, and Forward functionalities.


* **Real-time Polling & Notifications**: The client periodically checks for new data and alerts the user with pop-up windows.
* **Server Logging**: A dedicated GUI provides a real-time monitor of connections and email traffic.

---

## 🛠 How It Works

### Server

The server (`Server.java`) listens on port `8199`. For every incoming connection, it spawns a `ThreadConnection`.

* **Concurrency**: Uses thread synchronization to safely manage shared XML resources and user sessions.
* **Request Handling**: Processes `Request` objects for Login, Sending (InviaEmail), Reading (LeggiEmail), and Deletion (Cancellazione).

### Client

The client application (`Client.java`) follows the MVC pattern.

1. **Model**: `ModelClient` handles the logic and communication.
2. **View**: FXML files define the interface layout.
3. **Controller**: Manages the interaction between the user and the model, leveraging **Properties** to update the `ListView` and message panels.

---

## 📂 Project Structure

```text
.
├── src/
│   ├── Client/          # JavaFX code, Controllers, and Model
│   ├── Posta/           # Shared models (Request, PostaElettronica) for communication
│   └── Server/          # Connection handling and XML data logic
├── Luca@gmail.com.xml   # Local mailbox storage (User: Luca)
├── Davide@gmail.com.xml # Local mailbox storage (User: Davide)
└── Marco@gmail.com.xml  # Local mailbox storage (User: Marco)

```

---

## ⚙️ Setup and Usage

### Prerequisites

* **JDK 8** or higher.
* **JavaFX SDK** configured in your environment/IDE.

### Running the Application

1. **Start the Server**: Run `src/Server/Server.java`. The "Log Azioni" window will appear.
2. **Start the Client**: Run `src/Client/Client.java`.
3. **Login**: Use one of the pre-configured accounts:
* `Luca@gmail.com`
* `Davide@gmail.com`
* `Marco@gmail.com`

## 👥 Contributors

* **Davide Robustelli** — [@xDavikx](https://github.com/xDavikx)
* **Luca Robustelli** - [@LucaRobus99](https://github.com/LucaRobus99)
---

---

## 👤 Author

Project developed for the **Programming III** course – **University of Turin (UniTo)**.

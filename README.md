# Repsy API Project

## 📚 About
This project is a **Spring Boot REST API** service that lets you upload and download `.rep` packages, with additional meta files `meta.json`.  
It uses a **PostgreSQL** database and can optionally work with **MinIO** storage integration.

The app can be easily spun up using **Docker Compose**.

---

## 🚀 Getting Started

To run the project from the terminal, while you're in the root directory:

```bash
docker-compose up -d
```

Docker will automatically pull the necessary images and start the service.

Once it's up, you can access it at `localhost:8080`.

---

## 🛠 Requirements

- Docker
- Docker Compose
- (Optional) A Postgres and MinIO server if you want to run your own

---

## 🐳 Docker Image

- Application Image:  
  `repo.repsy.io/huzuntu/repsy-project-docker/repsy-api:1.0.0`

---

## ⚙️ Environment Variables

These are already set up inside `docker-compose.yml`:

| Variable | Description |
|:---|:---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password |
| `STORAGE_STRATEGY` | Choose between `file-system` or `object-storage` |
| `MINIO_ENDPOINT` | MinIO server URL (optional) |
| `MINIO_ACCESS_KEY` | MinIO access key |
| `MINIO_SECRET_KEY` | MinIO secret key |
| `MINIO_BUCKET_NAME` | Bucket name for MinIO |

---

## 🏗 Project Structure

```plaintext
repsy-api-project/
├── README.md
├── docker-compose.yml
├── data/
│   └── mypkg/
│       └── 1.0.0/
│           └── mypkg-1.0.0.rep
├── repsy-api/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── settings.xml
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── io/repsy/repsy_api/
│       │   │       ├── RepsyApiApplication.java
│       │   │       ├── config/
│       │   │       ├── controller/
│       │   │       ├── entity/
│       │   │       ├── exception/
│       │   │       ├── repository/
│       │   │       └── service/
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── static/
│       │       └── templates/
│       └── test/
├── repsy-libraries/
│   ├── storage-strategy/
│   ├── storage-filesystem/
│   └── storage-object/
```

---

## 📦 Package Hosting (Repsy.io)

- The **libraries** (`storage-strategy`, `storage-filesystem`, `storage-object`) are uploaded to a **private Maven repo** on Repsy.io.
- The **application** (`repsy-api`) is pushed as a **public Docker image** to Repsy.io.

---

## ✅ Setup and Testing Steps

1. Move to the project root:

    ```bash
    cd repsy-api-project
    ```

2. Start everything up:

    ```bash
    docker-compose up -d
    ```

3. Access the API:
    - `http://localhost:8080`

4. Try uploading or downloading a package to test.

---

## 📜 Notes

- If you're connecting to external PostgreSQL or MinIO services, make sure to update your environment variables in `docker-compose.yml`.
- The `data/` folder already has a sample `.rep` file for testing.

---

## ✨ Contact

- Developer: Umut Tölek
- Email: umuttolekk@gmail.com

---

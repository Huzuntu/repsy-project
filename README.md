# Repsy API Project

## 📚 About

This project is a **Spring Boot REST API** service that lets you upload and download `.rep` packages along with their `meta.json` metadata files.  
It uses a **PostgreSQL** database and can optionally work with **MinIO** for object storage.

The app can be easily run using **Docker Compose**.

---

## 🚀 Getting Started

1. **Clone** the project and move to the root directory.

2. **Start** the containers:

```bash
docker-compose up -d
```

Docker will automatically pull the necessary images and start:
- Postgres
- MinIO
- The Repsy API application

Once it's running, the API will be available at:  
➡️ `http://localhost:8080`

---

## 🛠 Requirements

- Docker
- Docker Compose

(Optional if not using Docker: PostgreSQL and MinIO servers)

---

## 🐳 Docker Images

- **Application Image**:  
  `repo.repsy.io/huzuntu/repsy-project-docker/repsy-api:1.0.0`

---

## ⚙️ Environment Variables

Environment variables are configured inside `docker-compose.yml`:

| Variable | Description |
|:---|:---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password |
| `STORAGE_STRATEGY` | Choose between `file-system` or `object-storage` |
| `MINIO_ENDPOINT` | MinIO server URL |
| `MINIO_ACCESS_KEY` | MinIO access key |
| `MINIO_SECRET_KEY` | MinIO secret key |
| `MINIO_BUCKET_NAME` | MinIO bucket name (default: `repsy-bucket`) |

---

## 📦 Project Structure

```plaintext
repsy-api-project/
├── README.md
├── docker-compose.yml
├── data/
│   └── mypkg/
│       └── 1.0.0/
│           ├── mypkg-1.0.0.rep
│           └── meta.json
├── repsy-api/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── settings.xml
│   └── src/
├── repsy-libraries/
│   ├── storage-strategy/
│   ├── storage-filesystem/
│   └── storage-object/
```

---

## ⚡️ Important Note for MinIO Testing

If you are using the `object-storage` strategy (MinIO):
- Please **manually create the bucket** before uploading files!
- Access MinIO web UI at:  
  ➡️ `http://localhost:9000`
- Default credentials:
    - Username: `minioadmin`
    - Password: `minioadmin`
- Create a bucket named `repsy-bucket`).

---

## ✅ How to Test

After containers are up:

### 1. Uploading a Valid Package

```bash
curl -X POST http://localhost:8080/mypkg/1.0.0 \
  -F "repFile=@./data/mypkg/1.0.0/mypkg-1.0.0.rep" \
  -F "metaFile=@./data/mypkg/1.0.0/meta.json"
```

✅ You should get a success message:  
`Package uploaded successfully`

---

### 2. Downloading the Package

```bash
curl -X GET http://localhost:8080/mypkg/1.0.0/mypkg-1.0.0.rep --output downloaded.rep
```

✅ It should download the file without any errors.

---

### 3. Testing Invalid Uploads

**a) Upload without `.rep` file**

```bash
curl -X POST http://localhost:8080/mypkg/1.0.1 \
  -F "metaFile=@./data/mypkg/1.0.0/meta.json"
```
❌ Should return an error saying `.rep file is missing`.

---

**b) Upload with invalid `meta.json`**

For example, use a `meta.json` with missing fields like (missing `name`, `version`, or `author` fields).

```bash
curl -X POST http://localhost:8080/mypkg/1.0.2 \
  -F "repFile=@./data/mypkg/1.0.0/mypkg-1.0.0.rep" \
  -F "metaFile=@./data/mypkg/1.0.0/invalid-meta.json"
```
❌ Should return an error about `meta.json` structure.

---

### 4. Switching Between FileSystem and MinIO Storage

- To switch between storage types, change this line in `docker-compose.yml`:

```yaml
STORAGE_STRATEGY: file-system
```
or

```yaml
STORAGE_STRATEGY: object-storage
```

Then restart containers:

```bash
docker-compose down
docker-compose up -d
```

---

## 📜 Notes

- If using **file-system** strategy, files are stored inside the container filesystem under `/data`.
- If using **object-storage** strategy, files are stored in your MinIO bucket.
- If MinIO bucket is missing, you must create it manually before testing.

---

## ✨ Contact

- Developer: Umut Tölek
- Email: umuttolekk@gmail.com

---
# Areal API Documentation

🙋‍♂️ Welcome to the Areal API documentation! This API provides a comprehensive set of endpoints for managing various aspects of the Areal system.

🚂 Areal helps you manage and streamline every aspect of your mortgage closing process.

## Document Processing Flow

The document processing functionality is the core of the Areal system, enabling users to upload PDF documents and automatically extract structured data through AI-powered classification and extraction services.

### High-Level Overview

The document processing flow transforms a single uploaded PDF into multiple classified documents with extracted structured data. This process involves several microservices working together to analyze, classify, and extract information from mortgage documents.

### Sequence Diagram

```mermaid
sequenceDiagram
    participant User as 👤 User
    participant API as 🖥️ Areal API
    participant S3 as 📦 AWS S3
    participant AI as 🤖 AI Services

    %% Phase 1: Upload Preparation and Initiation
    rect rgb(200,200,255)
    User->>+API: Request upload URLs
    API-->>-User: Pre-signed URLs + session_id
    User->>+S3: Upload PDF
    S3-->>-User: Upload confirmedc
    User->>+API: Start processing
    API-->>-User: Processing started
    end

    %% Phase 2: Classification
    rect rgb(220,255,220)
    API->>AI: Send for classification
    AI-->>API: Classification results
    API->>User: Real-time update (classification done)
    end

    %% Phase 3: Extraction
    rect rgb(255,240,200)
    API->>AI: Send for extraction
    AI-->>API: Extraction results
    API->>User: Real-time update (extraction done)
    end

    %% Phase 4: Finalization
    rect rgb(255,220,220)
    API->>User: Email notification (processing completed)
    end

    Note over User: Multiple documents ready with extracted data
```

See the [Core Process](core/index.md) for more details.
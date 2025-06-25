# Document Processing Flow

The document processing functionality is the core of the Areal system, enabling users to upload PDF documents and automatically extract structured data through AI-powered classification and extraction services.

## High-Level Overview

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

#### Phase 1: Upload Preparation and Initiation
1. User requests pre-signed URLs to upload documents. See [Upload Preparation](https://dev-api.v2.areal.ai/api/v2/docs#/processing/api_views_processing_get_presigned_url)
2. User uploads PDF(s) directly to S3 using the provided URLs. 
3. User tells the system to start processing the uploaded document(s). See [Start Processing](https://dev-api.v2.areal.ai/api/v2/docs#/processing/api_views_processing_start_processing)

#### Phase 2: Classification
- The system analyzes the uploaded PDF(s) and classifies the document types.
- User receives a real-time notification from the API when classification is complete.
- See [Classification](classification.md) section for details.

#### Phase 3: Extraction
- The system extracts structured data from the classified documents.
- User receives a real-time notification from the API when extraction is complete.
- See [Extraction](extraction.md) section for details.

#### Phase 4: Finalization
- User receives an email notification from the API when processing is complete and documents are ready for review.

!!! warning "Asynchronous Processing"
    The entire flow is asynchronous with callback-based communication. So if you are planning to integrate your software with Areal, you need to be prepared to handle the callback.

## Example Usage

```python
import requests
from pathlib import Path

BASE_PATH = Path(__file__).parent

# 0. Login
cookies = ... # get from auth.md

# 1. Get presigned URLs
file_names = ['sample.pdf']
response = requests.post(
    f'{base_url}/processing/presigned_url/',
    # params={'upload_session_id': upload_session_id}, -> for uploading to same session
    json={'file_names': file_names},
    cookies=cookies,
)
presigned_urls = response.json()['presigned_urls']
upload_session_id = response.json()['upload_session_id']

# 2. Upload to S3
response = requests.post(
    presigned_url['url'],
    data=presigned_url['fields'],
    files={'file': (BASE_PATH / 'sample.pdf').read_bytes()},
)
if response.status_code != 204:
    raise Exception(f'Failed to upload to S3: {response.status_code}')

# 3. Start processing flow
process_response = requests.post(
    f'{base_url}/processing/upload/',
    json={
        'upload_session_id': upload_session_id,
        'document_id': document_id,
        'file_name': file_name,
    },
    cookies=cookies,
)
```

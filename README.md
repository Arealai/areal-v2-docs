
# Areal Backend V2

Areal Backend V2 is a RESTful API that provides endpoints for the Areal project.
Its responsible for
- Handle Authentication and Permissions
- Database Interactions
- Gateway Interactionsx
- Email Notifications
- Live Status Updates & Messaging over WebSockets
- TechStack: 
    - Framework: Django, DjangoNinja
    - Testing: pytest, pytest-django, pytest-docker, pytest-cov
    - Cloud Services: AWS S3, SQS, SES 

## Installation

1. Clone the repository
2. Install the dependencies: `uv sync`
3. Run server using: `cd app && python manage.py runserver`

## API Documentation

OpenAPI Specs are at `http://localhost:8000/api/v2/#docs`
And Internal Specs are at `http://localhost:8000/internal/#docs`

## Testing

Run `pytest` for unit tests, and `pytest --integration` for integration tests.

For integrations tests we need LocalStack for AWS Service emulation, 
so we utilize docker to install it

## Local Development

You can use `make compose` for setting our services up (classification, extraction, cdbalancer etc.)
Alongside them a PostgreSQL database and LocalStack service is setup as well.
For Debuging you can use VSCode debugger which is already setup under APIV2-Debug

## CI/CD

We use `GitHub Actions` for running tests-deploy pipeline

Each push/pr triggers a deployment action



---
title: Migration Guide
description: Migrate an Areal V1 API integration to Areal V2.
---

If you are a customer of Areal V1, this guide is designed to help you migrate to Areal V2 as smoothly as possible. 

:::caution[Areal Dashboard]
For **Areal Dashboard** users, you can simply go into the new dashboard and start using it, guide is for API integrations with Areal
:::

## Creating Your Organization & Users in V2

We added a simple yet powerful **Organization Management** feature to V2!

You can decide **Organization Administrator** roles amongst your users and this role can manage your organization.
Please contact Areal to create your Administrator, which then invites users to their organization to assamble their team. We have further fine-grained roles that you can establish within you team. See [Profiles Docs](../auth.md) for details.


## Authentication

We reshaped the authentication mechanism in V2 with a focus on industry-standard security pratices!

We provide token-based (JWT) authentication, with short-lived access tokens and longer-lived refresh tokens so that you can request new access tokens as soon as they expire using the refresh token. Logouts fully invalidate both the access and the refresh token. For details see [Authentication Docs](../auth.md)

For code examples see [Authentication Examples](examples.md#authentication)

## Document Processing

We modified the processing API to serve you the easiest experience of using it

In V1 we required you to send the `base64` formatted data of your document which caused bottlenecks in large document sizes.

In V2 we follow better practices, firstly we ask you to request a PreSigned-URL from our API. This is a secure URL that you upload your files to. Since this is a server designed specifically for document uploads, it handles large files much more smoothly. Secondly, you request our API to start the processing. 

See [Document Processing Docs](../processing/processing.md) for a visual explanation and detailsd breakdown

See [Processing API Documentation](https://dev-api.v2.areal.ai/api/v2/docs#) for API specification for **PreSignedUrl** and **StartProcessing** requests.

See [Processing Code Examples](examples.md#DocumentProcessing)


## Processing Status Tracking

In V1 we had a long-running async processing mechanism that require you to constantly check weather the processing is completed or not. 

In V2 we decided to tackle this painpoint and build mechanisms that can help you to receive real-time updates about your documents processing status.

### Live Status Update over WebSocket 

You can connect to our WebSocket server over `wss://dev-api.v2.areal.ai/status/` using your access token to receive status updates about your document processing. For learning more about status see [Status Tracking Docs](../processing/status.md)

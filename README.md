# Space Mock

This is a server that acts as an intermediary between the backend and client devices, automatically storing the responses returned by the remote server in local files. It is capable of functioning as a mock server, returning the responses it has previously collected.

The server has a web-based administration frontend where the behavior mode can be selected, as detailed below.

It allows storing different sessions, so that you can later select the scenario you want to reproduce. This way, you can choose one of the users previously stored at any time.

![22](https://github.com/bernatdelgado87/KtorMocker/assets/52576076/bf3c8e73-01c9-48ba-8d63-4a676b890a75)

## How it works

Frontend: `http://localhost:5000/`
The project is currently set up to work locally because if it were to work remotely, all clients would listen to the same mock (the last one selected).
For now, it's only enabled to make SSL requests. If you want to mock the response of 'https://example.com/register', you should use the endpoint 'http://YourLocalIp:5000/example.com/register'.

![1](https://github.com/bernatdelgado87/KtorMocker/assets/52576076/256ed6d6-2350-471e-a0a0-dbb302a60fa9)


Essentially, it has two operating modes:
Local Mode and Remote Mode, which are explained below.

### Remote mode

![11](https://github.com/bernatdelgado87/KtorMocker/assets/52576076/8200b1d9-000d-49a7-a135-f261b4a27109)

When we activate remote mode, the server acts as an intermediary, forwarding the request to the backend and saving all the responses in a response store.

These responses will be saved in a new mock directory (button 'Listen new') or can overwrite an existing one (button 'Rewrite mock from local')


### Local mode

![local](https://github.com/bernatdelgado87/KtorMocker/assets/52576076/54a6cf89-b844-4108-a72f-a371d31e67b8)

Through the 'Local Mode' button, we can select the stored mock for our server to act by returning the responses stored in it.

In this local mode, our server will not call the backend to obtain a response but will return the stored response.

Additionally, if you forgot to store a response for a specific mocked session, or if you want to update the response to a specific endpoint by overwriting it, you can do so as you wish pressing button “Rewrite mock from remote”.

## Additional Features:

Space Mock stores each call, differentiating them based on their URL. Nevertheless, some special use cases have been considered, allowing you to add rules for particular uses:

![rules](https://github.com/bernatdelgado87/KtorMocker/assets/52576076/b463f516-4cf7-425f-96a4-c58bbbf7f9c9)


### 1. Mock Body Rules:

If your backend employs different services hosted on the same URL and distinguished by a field in the request body, you can instruct Space Mock to treat them as distinct calls, even when querying the same URL. To achieve this, you need to specify the differentiating field in the "Mock Body Rules" section on the frontend.

For instance, imagine you're querying the same endpoint called "https://example/users", where your backend registers or logs in users based on the request body:
```
{
   "method": "register"
}

```
Or
```
{
   "method": "login"
}

```
In this case, by introducing the Body Rule "method", Space Mock will create a file named "users/register" to store registration calls and another file "users/login" to store login calls.

### 2. Mock URL Rules:

Conversely, the "Mock URL Rules" feature handles scenarios where you want consistent responses regardless of the query parameters. For example, suppose you're querying an endpoint that returns course details:

`https://example/course?id=25`

By default, Space Mock generates a mocked file for each unique URL. If you want the response to be the same for all calls to this endpoint, regardless of the id parameter, you can specify the rule "id=" in the "Mock URL Rules" section. This instructs Space Mock to return the same mock for all calls to that endpoint, regardless of the id provided.

## Colab:

Of course, any suggestions or collaboration will be welcome.

# web-session-auth *by NilsCoding*

*web-session-auth* is a tiny Servlet Filter based authentication system which uses a session value to grant or revoke access.

It supports a custom pre-authenticate implementation to be processed first. This enables you to write authentication code to be executed first, which can perform the necessary lookup etc. to do a login (by storing the corresponding session value).

# Overview
                                                          (allow) --> continue filter chain (in webapp)  
    Incoming request --> Pre-Auth --> check session value ---|
                                                           (deny) --> send http code or redirect
  

# Documentation

(Sorry, not available yet. I'm working on that, promised!)

# Example filter configuration (web.xml)

    <filter>
        <filter-name>sessionAuthFilter</filter-name>
        <filter-class>com.github.nilscoding.sessionauth.SessionAuthFilter</filter-class>
        <init-param>
            <param-name>preAuthHandler</param-name>
            <param-value>my.package.AwesomeAuthHandler</param-value>
            <description>class name for custom IPreAuthHandler implementation</description>
        </init-param>
		<!-- the following parameters are optional -->
        <init-param>
            <param-name>whitelistUrlRegex</param-name>
            <param-value>^abc$</param-value>
            <description>url pattern for whitelist, will be checked against httpRequest.getRequestURI(), pre-auth will be done anyway</description>
        </init-param>
        <init-param>
            <param-name>sessionParam</param-name>
            <param-value>authGranted</param-value>
            <description>name of session parameter to check</description>
        </init-param>
        <init-param>
            <param-name>denyAction</param-name>
            <param-value>httpReturn=403,message=No access.</param-value>
            <description>action to perform if access is denied, possible values:
                httpReturn=[code]
                redirect=[location]
            </description>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>sessionAuthFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

# Contribute

Feel free to fork *web-session-auth* and add more useful functions to it. Or just implement you own authentication handler.

Found a bug? Please hunt it down by debugging first (if possible) and then create an issue on GitHub. I'll try my best to fix it then.

# Copyright / License

*web-session-auth* is licensed under the MIT License

## The MIT License (MIT)

Copyright (c) 2016 NilsCoding

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

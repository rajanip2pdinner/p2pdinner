//
//  ServiceConstants.h
//  P2PDinner
//
//  Created by sudheerkumar on 3/1/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#ifndef P2PDinner_ServiceConstants_h
//TypeDef POST GET
#define RequestTypeGet @"GET";
#define RequestTypePost  @"POST";
#define RequestTypePut @"PUT"
//RequestType
#define MIMETypeJSON  @"application/json";
#define MIMETypeFormURLEncoded  @"application/x-www-form-urlencoded";
#define MIMETypeXML @"application/xml";
#define MIMETypeTextXML  @"text/xml";
#define MIMETypeTextHtml  @"text/html";
#define MIMETypeTextJson  @"text/json";
#define MIMETypeTextPlain  @"text/plain";
typedef void(^ServiceResultBlock)(NSError *error, id response);
typedef void(^CompletionBlock)(void);
#define P2PDinner_ServiceConstants_h


#endif

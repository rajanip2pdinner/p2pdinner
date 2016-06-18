//
//  LoginResponce.m
//  P2PDinner
//
//  Created by Selvam M on 3/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "LoginResponce.h"

@implementation LoginResponce
- (LoginResponce *)mapObjectFromResponce:(id)response{
    NSDictionary *reponceDic=(NSDictionary *)response;
    [self setStatus:[reponceDic objectForKey:@"status"]];
    [self setMessage:[reponceDic objectForKey:@"message"]];
    [self setCode:[reponceDic objectForKey:@"code"]];
    return self;
}
@end

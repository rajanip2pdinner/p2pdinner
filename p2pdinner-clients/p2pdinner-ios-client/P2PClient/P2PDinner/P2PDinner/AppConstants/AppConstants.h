//
//  AppConstants.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

//  #import <Foundation/Foundation.h>
//
//  @interface AppConstants : NSObject
//
//  @end
#import "StoryBoardConstants.h"
#import "NumericLiteralConstants.h"
#import "StringConstants.h"
#define SYSTEM_VERSION_EQUAL_TO(v)                  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedSame)
#define SYSTEM_VERSION_GREATER_THAN(v)              ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedDescending)
#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN(v)                 ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN_OR_EQUAL_TO(v)     ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedDescending)

#define BASE_URL @"https://dev-p2pdinner-services.herokuapp.com/"
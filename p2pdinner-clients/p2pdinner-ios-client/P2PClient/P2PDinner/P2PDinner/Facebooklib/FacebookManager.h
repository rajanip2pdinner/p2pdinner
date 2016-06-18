//
//  FacebookManager.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <FacebookSDK/FacebookSDK.h>
#import "FBUserInfo.h"

@interface FacebookManager : NSObject{
    
}
//block for faceboook success action.
typedef void (^FacebookAction)(void);
//block to return userinfo.
typedef void (^FacebookUserInfoAction)(FBUserInfo* albumsObj);
//block to return userDetails
typedef void (^FacebookUserDetailsAction)(FBUserInfo* albumsObj,NSError *error);
//block for faceboook failure action.
typedef void (^FacebookFailureAction)(NSError *error);
+ (id)sharedFacebookManager;
- (void)getUserInfoWithBlockAction:(FacebookUserInfoAction)fbAction failureAction:(FacebookFailureAction)falureAction;
- (void)authorizeFacebook:(FacebookAction)fbAction failureAction:(FacebookFailureAction)falureAction;
- (void)logoutFacebook;


- (void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error userInfoAction:(FacebookUserDetailsAction)fbAction;
- (void)userLoggedIn;
- (void)userLoggedOut;

- (void)getProfileNameFromId:(NSString *)userId;

@end

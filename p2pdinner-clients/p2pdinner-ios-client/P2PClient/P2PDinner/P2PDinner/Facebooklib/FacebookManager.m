//
//  FacebookManager.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "FacebookManager.h"
static FacebookManager *_sharedInstance=nil;
static FBSession *fbSession=nil;
#define FB_APP_ID @"852778748075548"
#define Permission @[@"public_profile", @"email"]
@implementation FacebookManager
+ (id)sharedFacebookManager
{
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        _sharedInstance = [[self alloc] init];
    });
    return _sharedInstance;
}

- (void)authorizeFacebook:(FacebookAction)fbAction failureAction:(FacebookFailureAction)falureAction{
    if (fbSession == nil)
        fbSession = [[FBSession alloc] initWithAppID:FB_APP_ID permissions:Permission
                                     urlSchemeSuffix:nil
                                  tokenCacheStrategy:nil];
    [fbSession openWithBehavior:FBSessionLoginBehaviorForcingWebView completionHandler:^(FBSession *session, FBSessionState status, NSError *error)
     {
         
         
         switch (status)
         {
             case FBSessionStateOpen:
                 
                 [[FBRequest requestForMe] startWithCompletionHandler:
                  ^(FBRequestConnection *connection,
                    NSDictionary<FBGraphUser> *user,
                    NSError *error) {
                      if (!error) {
                          
                      }
                  }];
                 
                 
                 
                 fbSession=session;
                 fbAction();
                 break;
             case FBSessionStateClosedLoginFailed:
             { // prefer to keep decls near to their use
                 // unpack the error code and reason in order to compute cancel bool
                 // NSString *errorCode = [[error userInfo] objectForKey:FBErrorLoginFailedOriginalErrorCode];
                 //  NSString *errorReason = [[error userInfo] objectForKey:FBErrorLoginFailedReason];
                 // BOOL userDidCancel = !errorCode && (!errorReason ||[errorReason isEqualToString:FBErrorLoginFailedReasonInlineCancelledValue]);
                 
                 // call the legacy session delegate
                 // [self fbDialogNotLogin:userDidCancel];
             }
                 break;
             default:
                 break;
                 
         }
         if (error)
         {
             falureAction(error);
         }
     }];
}
- (void)fbDialogLogin:(NSString *)token expirationDate:(NSDate *)expirationDate
{
    
}
- (void)fbDialogNotLogin:(BOOL)cancelled
{
}

-(void)getUserInfoWithBlockAction:(FacebookUserInfoAction)fbAction failureAction:(FacebookFailureAction)falureAction{
    
    if (FBSession.activeSession.isOpen) {
        
        [[FBRequest requestForMe] startWithCompletionHandler:
         ^(FBRequestConnection *connection,
           NSDictionary<FBGraphUser> *user,
           NSError *error) {
             if (!error) {
                 FBUserInfo* albumsObj=[[FBUserInfo alloc] init];
                 [albumsObj setUid:@"testid"];
                 [albumsObj setFirst_name:@"TestFirstname"];
                 [albumsObj setName:@"name"];
                 [albumsObj setPic:@"http://test"];
                 [albumsObj setPic:@"test"];
                 fbAction(albumsObj);
                 
             }
             else{
                 falureAction(error);
             }
         }];
    }
    
}
- (void)logoutFacebook
{
    //clearing facebook cookies .
    [FBSession.activeSession closeAndClearTokenInformation];
    
    //clearing facebook cookies for webview.
    NSHTTPCookieStorage *cookies = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    NSArray *facebookCookies = [cookies cookiesForURL:
                                [NSURL URLWithString:@"http://login.facebook.com"]];
    
    for (NSHTTPCookie *cookie in facebookCookies)
    {
        [cookies deleteCookie:cookie];
    }
    //cookies removed from fbdialog url-subramani.c
    NSHTTPCookieStorage *cookiesInDialog = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    NSArray *facebookCookiesInDialog = [cookiesInDialog cookiesForURL:
                                        [NSURL URLWithString:@"https://m.facebook.com/dialog/oauth?"]];
    
    for (NSHTTPCookie *cookie in facebookCookiesInDialog)
    {
        [cookies deleteCookie:cookie];
    }
    
}


#pragma requestGraphApi method

-(void)requestGraphApi:(NSString*)graphPath withCompetion:(void (^) (FBRequestConnection *connection, id result, NSError *error)) competion{
    
    [FBRequestConnection startWithGraphPath:graphPath
                                 parameters:nil
                                 HTTPMethod:@"GET"
                          completionHandler:^(FBRequestConnection *connection, id result, NSError *error)
     {
         competion(connection, result, error);
         
     }];
}
-(void)getUserDetails:(FacebookUserDetailsAction)fbAction{
    [[FBRequest requestForMe] startWithCompletionHandler:
     ^(FBRequestConnection *connection,
       NSDictionary<FBGraphUser> *user,
       NSError *error) {
         
         if (!error) {
             FBUserInfo *userInfo=[[FBUserInfo alloc]init];
             userInfo.first_name = user.first_name;
             userInfo.name = user.name;
             userInfo.uid= user.objectID;
             userInfo.emailId = [user objectForKey:@"email"];
             userInfo.pic_square = [[NSString alloc] initWithFormat: @"http://graph.facebook.com/%@/picture?type=large", user.objectID];
             fbAction(userInfo,error);
             
         }
         fbAction(nil,error);
     }];
    
}

#pragma fbsession handle methods
// This method will handle ALL the session state changes in the app
- (void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error userInfoAction:(FacebookUserDetailsAction)fbAction
{
    // If the session was opened successfully
    if (!error && state == FBSessionStateOpen){
        NSLog(@"Session opened");
        // Show the user the logged-in UI
        [self userLoggedIn];
        [self getUserDetails:fbAction];
        return;
    }
    if (state == FBSessionStateClosed || state == FBSessionStateClosedLoginFailed){
        // If the session is closed
        NSLog(@"Session closed");
        // Show the user the logged-out UI
        [self userLoggedOut];
        return ;
    }
    
    // Handle errors
    if (error){
        NSLog(@"Error");
        NSString *alertText;
        NSString *alertTitle;
        // If the error requires people using an app to make an action outside of the app in order to recover
        if ([FBErrorUtility shouldNotifyUserForError:error] == YES){
            alertTitle = @"Something went wrong";
            alertText = [FBErrorUtility userMessageForError:error];
            [self showMessage:alertText withTitle:alertTitle];
        } else {
            
            // If the user cancelled login, do nothing
            if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                NSLog(@"User cancelled login");
                
                // Handle session closures that happen outside of the app
            } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession){
                alertTitle = @"Session Error";
                alertText = @"Your current session is no longer valid. Please log in again.";
                [self showMessage:alertText withTitle:alertTitle];
                
                // For simplicity, here we just show a generic message for all other errors
                // You can learn how to handle other errors using our guide: https://developers.facebook.com/docs/ios/errors
            } else {
                //Get more error information from the error
                NSDictionary *errorInformation = [[[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"] objectForKey:@"body"] objectForKey:@"error"];
                
                // Show the user an error message
                alertTitle = @"Something went wrong";
                alertText = [NSString stringWithFormat:@"Please retry. \n\n If the problem persists contact us and mention this error code: %@", [errorInformation objectForKey:@"message"]];
                [self showMessage:alertText withTitle:alertTitle];
            }
        }
        // Clear this token
        [FBSession.activeSession closeAndClearTokenInformation];
        // Show the user the logged-out UI
        [self userLoggedOut];
    }
}

// Show the user the logged-out UI
- (void)userLoggedOut
{
    // Set the button title as "Log in with Facebook"
    // UIButton *loginButton = [self.customLoginViewController loginButton];
    // [loginButton setTitle:@"Log in with Facebook" forState:UIControlStateNormal];
    
    // Confirm logout message
    [self showMessage:@"You're now logged out" withTitle:@""];
}

// Show the user the logged-in UI
- (void)userLoggedIn
{
    // Set the button title as "Log out"
    
    // Welcome message
    
    [self showMessage:@"You're now logged in" withTitle:@"Welcome!"];
    
}
// Show an alert message
- (void)showMessage:(NSString *)text withTitle:(NSString *)title
{
    
}
- (void)getProfileNameFromId:(NSString *)userId{
//    [self requestGraphApi:[NSString stringWithFormat:@"/%@",userId] withCompetion:^(FBRequestConnection *connection, id result, NSError *error) {
//        
//    }];

    [[FBRequest requestForGraphPath:[NSString stringWithFormat:@"/%@",userId] ] startWithCompletionHandler:
    ^(FBRequestConnection *connection,
      NSDictionary<FBGraphUser> *user,
      NSError *error) {
        
        
    }];
}
@end

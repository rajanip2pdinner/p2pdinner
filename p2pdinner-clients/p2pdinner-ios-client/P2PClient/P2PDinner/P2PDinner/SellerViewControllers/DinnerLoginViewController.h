//
//  DinnerLoginViewController.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/12/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginResponce.h"
typedef void(^LoginResponseBlock)(NSError *error, NSString *emailId,BOOL successFull);

@interface DinnerLoginViewController : UIViewController{
    
}
@property (readwrite, nonatomic, copy) LoginResponseBlock loginResponseBlock;
- (IBAction)facebookLoginButtonAction:(id)sender;
- (void)getLoginResponse:(LoginResponseBlock)loginResponse;
@end

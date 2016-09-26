//
//  HomeViewController.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/25/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LaunchScreen.h"
#import "FacebookManager.h"
#import "LocationManger.h"

@interface HomeViewController : UIViewController<LocationManagerDelegate>{
    
}
-(void)moveToMyOrderScreen;
-(IBAction)tearmsAndCondition:(id)sender;
@end

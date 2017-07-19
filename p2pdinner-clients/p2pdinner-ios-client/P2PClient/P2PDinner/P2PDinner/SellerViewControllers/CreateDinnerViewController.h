//
//  CreateDinnerViewController.h
//  P2PDinner
//
//  Created by sudheerkumar on 2/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MenuViewController.h"
typedef enum {
    startTime,
    endTime,
    acceptOrderTime
    
}DinnerTime;
@interface CreateDinnerViewController : UIViewController{
    IBOutlet UITableView *tableView;
    
}
@property(nonatomic,assign)BOOL isFoodSafetyAvailable;
+ (void)backButtonAction;
@end

//
//  ViewController.h
//  P2PDinner
//
//  Created by P2PDinner on 2/3/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DatePicker.h"
#import "GuestTableviewCell.h"
#import "LocationManger.h"
#import "LocationServiceHandler.h"


@interface FindDinnerViewController : UIViewController<UITextFieldDelegate,DatePickerDelegate,LocationManagerDelegate>{
   IBOutlet UITableView *dinnerUISetup;
    //For TableView Selection
    UILabel *selectedDate;
    UITextField *selectedAddressField;
    NSDate *dinnerDate;
    GuestTableviewCell *guestTableViewCell;
    
    //Selected Value
    NSDate *findDinnerDateValue;
    NSString *findDinneraddressValue;
    int guestValue;
    IBOutlet UIButton *findDinnerButton;
    BOOL freeFoodEnable;
}
-(IBAction)findDinner:(id)sender;
@end


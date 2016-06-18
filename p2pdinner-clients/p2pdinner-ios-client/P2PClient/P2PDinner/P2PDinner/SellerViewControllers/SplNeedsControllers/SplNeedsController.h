//
//  SplNeedsController.h
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "specialNeedsCell.h"
#import "ItemDetails.h"

@interface SplNeedsController : UIViewController
{
    UISwitch *veganSwitch;
    UISwitch *kosherSwitch;
    UISwitch *vegetarianSwitch;
    UISwitch *glutenFreeSwitch;
    UISwitch *halalSwitch;
    UISwitch *diabetic;
    UISwitch *lowCarb;
    UISwitch *lowFatDiet;
}
@property(nonatomic,strong) ItemDetails *itemDetails;
-(IBAction)updateItem:(id)sender;
@end

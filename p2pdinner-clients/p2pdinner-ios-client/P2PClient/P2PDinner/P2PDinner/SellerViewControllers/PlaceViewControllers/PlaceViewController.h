//
//  PlaceViewController.h
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetails.h"
#import "LocationManger.h"

@interface PlaceViewController : UIViewController
{
    UISwitch *eateIn;
    UISwitch *toGo;
}
@property(nonatomic,strong) ItemDetails *itemDetails;
@property(nonatomic,strong) IBOutlet UITableView *placeTableView;
@property(nonatomic,strong) UITextView *textVeiw;
-(IBAction)updateItem:(id)sender;
-(IBAction)editButtonAction:(id)sender;
@end

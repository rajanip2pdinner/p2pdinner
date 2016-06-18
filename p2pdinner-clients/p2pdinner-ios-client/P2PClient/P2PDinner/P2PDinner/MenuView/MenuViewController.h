//
//  MenuViewController.h
//  P2PDinner
//
//  Created by Selvam M on 4/16/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetails.h"
#import "SellerHistoryHandler.h"
#import "ItemDetailsShared.h"


@interface MenuViewController : UIViewController
{
    IBOutlet UIButton *nextButton;
}

@property(nonatomic, weak)IBOutlet UIView *displayView;
@property(nonatomic, strong)ItemDetails *itemDetails;
-(IBAction)nextButtonAction:(id)sender;
- (void)updateMenuItem:(ItemDetails *)itemDetails1;
@end

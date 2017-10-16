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
typedef void(^updateCallback)();

@interface MenuViewController : UIViewController
{
    IBOutlet UIButton *nextButton;
}

@property(nonatomic, weak)IBOutlet UIView *displayView;
@property(nonatomic, weak)IBOutlet UINavigationBar *navigationBar;
@property(nonatomic, strong)ItemDetails *itemDetails;
@property(nonatomic, assign)updateCallback completed;
-(IBAction)nextButtonAction:(id)sender;
- (void)updateMenuItem:(ItemDetails *)itemDetails1;
- (void)updateMenuItem:(ItemDetails *)itemDetails1 withUpdateCallBack:(updateCallback)callBack;
@end

//
//  UIViewController+P2PViewController.m
//  P2PDinner
//
//  Created by Selvam M on 3/15/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "UIViewController+P2PViewController.h"
#import "HomeViewController.h"
#import "CreateDinnerViewController.h"
#import "MenuViewController.h"
#import "ItemDetailsShared.h"

@implementation UIViewController (P2PViewController)
- (void)updateMenuItemOnBackAction{
    MenuViewController *menuView=[[MenuViewController alloc]init];
    [menuView updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
}
- (void)backAction{
    if (![self isKindOfClass:[CreateDinnerViewController class]]) {
        [self.navigationController popViewControllerAnimated:YES];
        if ([self isKindOfClass:[UINavigationController class]]){
        [self updateMenuItemOnBackAction];
        }
        
    }
    else{
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
    
}
- (void)addBackButton{
    
    UIImage *leftimage = [UIImage imageNamed:@"Back@2x.png"];
    
    CGRect leftframeimg = CGRectMake(0, 0, leftimage.size.width, leftimage.size.height);
    
    UIButton *homeButton = [[UIButton alloc] initWithFrame:leftframeimg];
    [homeButton addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    [homeButton setImage:leftimage forState:UIControlStateNormal];
    leftimage = nil;
    //mobildev 3137 3/9/14
    
    UIBarButtonItem *leftBarButton = [[UIBarButtonItem alloc] initWithCustomView:homeButton];
    
    // adding flexible space for alignment
    UIBarButtonItem * flexibleSpace =[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace target:nil action:nil];
    if (![self isKindOfClass:[MenuViewController class]]) {
    [self.navigationItem setLeftBarButtonItems:[NSArray arrayWithObjects:flexibleSpace,leftBarButton,nil]];
    }
    leftBarButton = nil;
    flexibleSpace = nil;
    homeButton = nil;
}

- (void)viewDidLoad{
     self.navigationController.navigationBar.barStyle=UIBarStyleBlackTranslucent;
    if (![self isKindOfClass:[HomeViewController class]]) {
        [self addBackButton];
    }
    
}
@end

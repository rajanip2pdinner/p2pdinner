//
//  MyOrderDetail.h
//  P2PDinner
//
//  Created by Selvam M on 7/30/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyOrderDetail : UIViewController

@property (weak, nonatomic) IBOutlet UITableView *myOrderDetailTable;
@property (strong, nonatomic) NSArray *myOrderArray;
@property (strong, nonatomic) NSString *selectedMenu;
@property (strong, nonatomic) NSNumber *selectedMenuId;
@end

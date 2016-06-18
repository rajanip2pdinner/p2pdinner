//
//  MyOrderViewController.h
//  P2PDinner
//
//  Created by Selvam M on 7/22/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DateToolBar.h"

@interface MyOrderViewController : UIViewController<BarButtonActionDelegate>{
    IBOutlet UITableView *tableView;
    IBOutlet DateToolBar *dateToolBar;
    IBOutlet UISegmentedControl *segmentedControl;
}
@property(nonatomic,strong) NSArray *tableViewArray;
-(IBAction)myOrderDinnerOptions:(id)sender;
@end

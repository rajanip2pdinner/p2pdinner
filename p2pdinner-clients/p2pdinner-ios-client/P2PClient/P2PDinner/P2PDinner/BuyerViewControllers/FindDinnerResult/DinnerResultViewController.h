//
//  DinnerResultViewController.h
//  P2PDinner
//
//  Created by Selvam M on 8/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ActivityView.h"

@interface DinnerResultViewController : UIViewController{
    
}
@property(nonatomic,weak) IBOutlet UITableView *dinnerResultTable;
@property(nonatomic,strong)NSArray *dinnerListArray;
@end

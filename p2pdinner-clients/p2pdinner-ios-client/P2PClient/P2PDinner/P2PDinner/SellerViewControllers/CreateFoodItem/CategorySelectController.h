//
//  CategorySelectController.h
//  P2PDinner
//
//  Created by Selvam M on 3/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CategoryServiceHandler.h"
#import "CategoryItems.h"
@protocol CategorySelectDelegate<NSObject>
- (void)selectedCategors:(NSArray *)selectedArray;
@end

@interface CategorySelectController : UIViewController
{
    NSMutableArray *categoryList;
    NSMutableArray *selectedCategory;
    IBOutlet UITableView *categoryTableView;
    IBOutlet UINavigationBar *navigationBar;
    
}
@property(nonatomic,strong)id<CategorySelectDelegate> delegate;
- (IBAction)doneButtonAction:(id)sender;
@end

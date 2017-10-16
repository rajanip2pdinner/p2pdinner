//
//  CreateFoodItem.h
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TitleTableViewCell.h"
#import "ItemDetails.h"
#import "DescriptionTableViewCell.h"

@interface CreateFoodItem : UIViewController<UIGestureRecognizerDelegate>{
    NSArray *categoryArray;
    UILabel *itemCategoryLable;
}
@property(nonatomic,strong) ItemDetails *itemDetails;
@property(nonatomic,weak)IBOutlet UITableView *createFoodTable;
- (IBAction)textViewEndEditing:(id)sender;
@end

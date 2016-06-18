//
//  CostCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetailsShared.h"
@protocol CostCellDelegate<NSObject>
-(void)updatedItems;
@end
typedef enum{
    DecrementValue,
    IncrementValue
    
}ChangeValue;
@interface CostCell : UITableViewCell
{
    IBOutlet UISegmentedControl *pricePerMealSegment;
    IBOutlet UISegmentedControl *pricePerMealCentsSegment;
    IBOutlet UILabel *pricePerMealLable;
    IBOutlet UILabel *pricePerMealCentsLable;
}
- (void)setPrice:(NSNumber *)price;
- (NSNumber *)getCostForDinner;
@property(nonatomic,retain) id<CostCellDelegate> delegate;
- (IBAction)pricePerMealSegmentAction:(id)sender;
- (IBAction)priceCentsSegmentAction:(id)sender;

@end

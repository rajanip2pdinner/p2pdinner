//
//  MinimumOrdersCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol MinimumOrdersDelegate<NSObject>
- (void)updatedItems;
@end
@interface MinimumOrdersCell : UITableViewCell
{
     IBOutlet UILabel *maximumOrdersLable;
     IBOutlet UISegmentedControl *maximumOrdersSegmet;
}
- (void)setMaximuOrdersValue:(NSNumber *)value;
- (NSNumber *)getMaximuOrdersValue;
@property(nonatomic,retain)id<MinimumOrdersDelegate> delegate;
- (IBAction)maximumNumberOfOrdersAction:(id)sender;
@end

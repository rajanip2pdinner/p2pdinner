//
//  AcceptOrdersTableCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol AcceptOrdersDelegate<NSObject>
-(void)updatedItems;
@end


@interface AcceptOrdersTableCell : UITableViewCell
{
    IBOutlet UISegmentedControl *acceptOrdersSegmentButton;
    IBOutlet UILabel *availableFrom;
}
@property(nonatomic,retain)id<AcceptOrdersDelegate> delegate;
- (void)setAcceptOrdersTime:(NSDate *)acceptDate;
- (NSDate *)getAcceptOrdersTime:(NSDate *)selectedDate;
- (IBAction)acceptOrdersAction:(id)sender;
@end

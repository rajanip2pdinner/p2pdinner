//
//  AcceptOrdersTableCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@interface AcceptOrdersTableCell : UITableViewCell
@property(nonatomic,weak)IBOutlet UILabel *availableFrom;
- (void)setAcceptOrdersTime:(NSDate *)acceptDate;
//- (NSDate *)getAcceptOrdersTime:(NSDate *)selectedDate;
@end

//
//  AvialbleTimeTableCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetailsShared.h"


@interface AvialbleTimeTableCell : UITableViewCell
@property(nonatomic,weak)IBOutlet UILabel *fromLable;
@property(nonatomic,weak)IBOutlet UILabel *toLable;
//- (void)setTimeFrom:(NSDate *)startTime toTime:(NSDate *)endTime;
-(void)setStartTime:(NSDate *)startTime;
-(void)setEndTime:(NSDate *)endTime;
//- (NSDate *)getStartTime:(NSDate *)selectedDate;
//- (NSDate *)getStopTime:(NSDate *)selectedDate;
//- (IBAction)fromSegmentAction:(id)sender;
//- (IBAction)toSegmentAction:(id)sender;
//Reuse for next Cell
- (NSString *)calculateTimeOperation:(NSString *)curentTime operation:(NSInteger)integer;
- (NSString *)amPmConvertFromDate:(NSDate *)date;
- (NSString *)amPmRemoveDotFromString:(NSString *)dateStr;

@end

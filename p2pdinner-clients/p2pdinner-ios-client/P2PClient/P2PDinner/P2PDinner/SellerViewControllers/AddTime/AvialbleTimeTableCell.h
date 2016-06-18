//
//  AvialbleTimeTableCell.h
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetailsShared.h"
@protocol AvialbleTimeDelegate<NSObject>
-(void)updatedItems;
@end

@interface AvialbleTimeTableCell : UITableViewCell
{
    IBOutlet UISegmentedControl *fromSegmentButton;
    IBOutlet UISegmentedControl *toSegmentButton;
    
    IBOutlet UILabel *fromLable;
    IBOutlet UILabel *toLable;
}
@property(nonatomic,retain)id<AvialbleTimeDelegate> delegate;
- (void)setTimeFrom:(NSDate *)startTime toTime:(NSDate *)endTime;
- (NSDate *)getStartTime:(NSDate *)selectedDate;
- (NSDate *)getStopTime:(NSDate *)selectedDate;
- (IBAction)fromSegmentAction:(id)sender;
- (IBAction)toSegmentAction:(id)sender;
//Reuse for next Cell
- (NSString *)calculateTimeOperation:(NSString *)curentTime operation:(NSInteger)integer;
- (NSString *)amPmConvertFromDate:(NSDate *)date;
- (NSString *)amPmRemoveDotFromString:(NSString *)dateStr;

@end

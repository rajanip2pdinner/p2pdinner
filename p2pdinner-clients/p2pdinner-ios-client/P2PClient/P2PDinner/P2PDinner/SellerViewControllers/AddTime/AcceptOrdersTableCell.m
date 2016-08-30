//
//  AcceptOrdersTableCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AcceptOrdersTableCell.h"
#import "AvialbleTimeTableCell.h"
#import "Utility.h"
@interface AcceptOrdersTableCell(){
    AvialbleTimeTableCell *availableTime;
}
@end
@implementation AcceptOrdersTableCell

- (void)awakeFromNib {
    // Initialization code
    
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}
- (void)removeSelectedIndex:(UISegmentedControl *)segmentBtn{
    [segmentBtn setSelectedSegmentIndex:-1];
    
}
- (void)validateAvailableTimeObject{
    if (!availableTime) {
        availableTime=[[AvialbleTimeTableCell alloc]init];
    }
    
}
//- (IBAction)acceptOrdersAction:(id)sender{
//    UISegmentedControl *segment=(UISegmentedControl *)sender;
//    [self validateAvailableTimeObject];
//    availableFrom.text=[availableTime calculateTimeOperation:availableFrom.text operation:segment.selectedSegmentIndex];
//    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.5];
//}
-(NSDate *)getLocalTimeValue:(NSDate *)sourceDate{
    
    NSTimeZone* sourceTimeZone = [NSTimeZone timeZoneWithAbbreviation:@"UTC"];
    NSTimeZone* destinationTimeZone = [NSTimeZone systemTimeZone];
    
    NSInteger sourceGMTOffset = [sourceTimeZone secondsFromGMTForDate:sourceDate];
    NSInteger destinationGMTOffset = [destinationTimeZone secondsFromGMTForDate:sourceDate];
    NSTimeInterval interval = destinationGMTOffset - sourceGMTOffset;
    
    NSDate* destinationDate = [[NSDate alloc] initWithTimeInterval:interval sinceDate:sourceDate];
    return destinationDate;
}
- (void)setAcceptOrdersTime:(NSDate *)acceptDate
{
    acceptDate=[self getLocalTimeValue:acceptDate];
    [self validateAvailableTimeObject];
    _availableFrom.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:acceptDate  timeZone:LOCAL],[availableTime amPmConvertFromDate:acceptDate]];
}
- (NSDate *)getAcceptOrdersTime:(NSDate *)selectedDate{
    [self validateAvailableTimeObject];
    return [Utility stringToDateFormat:@"MM/dd/yyyy h.mm a" dateString:[NSString stringWithFormat:@"%@ %@",[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:selectedDate  timeZone:LOCAL],[availableTime amPmRemoveDotFromString:_availableFrom.text]] timeZone:LOCAL];
}

@end

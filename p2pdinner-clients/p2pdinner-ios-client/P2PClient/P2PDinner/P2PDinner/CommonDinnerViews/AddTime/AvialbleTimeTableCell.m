//
//  AvialbleTimeTableCell.m
//  P2PDinner
//
//  Created by Selvam M on 4/4/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AvialbleTimeTableCell.h"
#import "Utility.h"
#import "StringConstants.h"


@implementation AvialbleTimeTableCell


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

- (void)removeSelectedIndex:(UISegmentedControl *)segmentBtn{
    [segmentBtn setSelectedSegmentIndex:-1];
}

//- (IBAction)fromSegmentAction:(id)sender{
//#warning Need to add validation
//    [self timeChangeOperation:sender lable:fromLable];
//    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.5];
//}
//
//- (IBAction)toSegmentAction:(id)sender{
//    
//    [self timeChangeOperation:sender lable:toLable];
//    [self performSelector:@selector(removeSelectedIndex:) withObject:sender afterDelay:0.5];
//}

- (void)timeChangeOperation:(UISegmentedControl *)segment lable:(UILabel *)lable{
    lable.text=[self calculateTimeOperation:lable.text operation:segment.selectedSegmentIndex];
}
#pragma Date FormToValidation
- (void)validateDate:(NSDate *)fromDate toDate:(NSDate *)toDate{
    if( [fromDate timeIntervalSinceDate:toDate] > 0 ) {
        //NSLog(@"Correct");
    }
    else{
        // NSLog(@"Not Correct");
    }
    
}

#pragma DateOperations SegementControllers
- (NSString *)amPmConvertFromDate:(NSDate *)date{
    if (date==nil) {
        date=[NSDate date];
    }
    NSMutableString *amPmSrt=[NSMutableString stringWithString:[[Utility dateToStringFormat:kAMPM dateString:date timeZone:LOCAL] lowercaseString] ];
    [amPmSrt insertString:kDot_String atIndex:1];
    return [NSString stringWithFormat:kAppendWithDot_String,amPmSrt];
}

- (NSString *)amPmRemoveDotFromString:(NSString *)dateStr{
    NSArray *arrayOfString=[dateStr componentsSeparatedByString:kSpace_String];
    NSString *dateValue=[arrayOfString objectAtIndex:0];
    NSString *string=[arrayOfString objectAtIndex:1];
    NSString *amPmValue=[string stringByReplacingOccurrencesOfString:kDot_String withString:kEmpty_String];
    return [NSString stringWithFormat:k2StringAppendFormartWithSpace,dateValue,amPmValue];
}

- (NSString *)calculateTimeOperation:(NSString *)curentTime operation:(NSInteger)integer{
    NSDate *currentDate=[Utility stringToDateFormat:kTimeMinOnly12hrsFormat dateString:[self amPmRemoveDotFromString:curentTime]timeZone:LOCAL];
    if (integer==1) {
        currentDate=[currentDate dateByAddingTimeInterval:(15*60)];
    }else
        currentDate=[currentDate dateByAddingTimeInterval:-(15*60)];
    return [NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:currentDate timeZone:LOCAL],[self amPmConvertFromDate:currentDate]];
    
}
-(NSDate *)getLocalTimeValue:(NSDate *)sourceDate{

NSTimeZone* sourceTimeZone = [NSTimeZone timeZoneWithAbbreviation:kUTC];
NSTimeZone* destinationTimeZone = [NSTimeZone systemTimeZone];

NSInteger sourceGMTOffset = [sourceTimeZone secondsFromGMTForDate:sourceDate];
NSInteger destinationGMTOffset = [destinationTimeZone secondsFromGMTForDate:sourceDate];
NSTimeInterval interval = destinationGMTOffset - sourceGMTOffset;

NSDate* destinationDate = [[NSDate alloc] initWithTimeInterval:interval sinceDate:sourceDate];
    return destinationDate;
}
//- (void)setTimeFrom:(NSDate *)startTime toTime:(NSDate *)endTime{
//    startTime=[self getLocalTimeValue:startTime];
//    endTime=[self getLocalTimeValue:endTime];
//    fromLable.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:startTime timeZone:LOCAL],[self amPmConvertFromDate:startTime]];
//    toLable.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:endTime timeZone:LOCAL],[self amPmConvertFromDate:endTime]];
//}
-(void)setStartTime:(NSDate *)startTime{
    //startTime=[Utility getLocalTimeValue:startTime];
    _fromLable.text=[NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:startTime timeZone:LOCAL],[self amPmConvertFromDate:startTime]];
}
-(void)setEndTime:(NSDate *)endTime{
     //endTime=[self getLocalTimeValue:endTime];
     _toLable.text=[NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:endTime timeZone:LOCAL],[self amPmConvertFromDate:endTime]];
}
- (NSDate *)getStartTime:(NSDate *)selectedDate{
    
    
    return [Utility stringToDateFormat:kDateAndTime12hrsFormat dateString:[NSString stringWithFormat:k2StringAppendFormartWithSpace,[Utility dateToStringFormat:kDateMonthYearOnly dateString:selectedDate timeZone:LOCAL],[self amPmRemoveDotFromString:_fromLable.text]] timeZone:LOCAL];
}

- (NSDate *)getStopTime:(NSDate *)selectedDate{
//     NSDate *startDate=[Utility stringToDateFormat:@"MM/dd/yyyy h.mm a" dateString:[NSString stringWithFormat:@"%@ %@",[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:selectedDate timeZone:LOCAL],[self amPmRemoveDotFromString:_fromLable.text]] timeZone:UTC];
//    
//    NSDate *stopDate=[Utility stringToDateFormat:@"MM/dd/yyyy h.mm a" dateString:[NSString stringWithFormat:@"%@ %@",[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:selectedDate timeZone:LOCAL],[self amPmRemoveDotFromString:_toLable.text]] timeZone:UTC];
    
    
    return [Utility stringToDateFormat:kDateAndTime12hrsFormat dateString:[NSString stringWithFormat:k2StringAppendFormartWithSpace,[Utility dateToStringFormat:kDateMonthYearOnly dateString:selectedDate timeZone:LOCAL],[self amPmRemoveDotFromString:_toLable.text]] timeZone:UTC];
}
@end

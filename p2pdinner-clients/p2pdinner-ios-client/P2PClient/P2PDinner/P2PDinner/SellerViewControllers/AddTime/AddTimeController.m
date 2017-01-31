//
//  AddTimeController.m
//  P2PDinner
//
//  Created by Selvam M on 4/2/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "AddTimeController.h"
#import "Utility.h"
#import "ItemDetailsShared.h"
#import "SellerHistoryHandler.h"
#import "DateAndTimeController.h"
#import "StringConstants.h"

#define BetweenStartTimeAndEndTime 2
@interface AddTimeController ()
{
    
}

@end

@implementation AddTimeController
@synthesize itemDetails;
- (void)viewDidLoad {
    
    //NSLog(@"%@",itemDetails.startDate);
    [super viewDidLoad];
    startDate=[Utility stringToDateFormat:kDateAndTimeFormat dateString:itemDetails.startDate  timeZone:LOCAL];
    endDate=[Utility stringToDateFormat:kDateAndTimeFormat dateString:itemDetails.endDate  timeZone:LOCAL];
    closeDate=[Utility stringToDateFormat:kDateAndTimeFormat dateString:itemDetails.closeDate  timeZone:LOCAL];
    picSelectedDate=[NSDate date];
    self.title=kDinner_Listing;
    
    
}

-(void)timeValidationCheck{
    NSDate *calculatedDate;
    if ([endDate timeIntervalSinceNow] < 0.0||[closeDate timeIntervalSinceNow] < 0.0) {
        // Date has passed
        NSDate *mydate = [NSDate date];
        NSTimeInterval nextHourInterveral = 1 * 60 * 60;
        calculatedDate=[mydate dateByAddingTimeInterval:nextHourInterveral];
        NSString *calculatedDateString=[Utility dateToStringFormat:kDateAndTimeFormat dateString:calculatedDate timeZone:UTC];
        endDate =[Utility stringToDateFormat:kDateAndTimeFormat dateString:calculatedDateString  timeZone:LOCAL];
        closeDate=endDate;
    }
    NSString *currentDate=[Utility dateToStringFormat:[NSDate date] timeZone:UTC];
    NSString *endDateString=[Utility dateToStringFormat:calculatedDate timeZone:LOCAL];
    
    if (![currentDate isEqualToString:endDateString]) {
        NSString *calculatedDateString=[Utility dateToStringFormat:kDateAndTimeFormat dateString:[Utility endOfDay:[NSDate date]] timeZone:UTC];
        endDate =[Utility stringToDateFormat:kDateAndTimeFormat dateString:calculatedDateString  timeZone:LOCAL];
        closeDate=endDate;
    }
    
}
-(BOOL)validateNextDate:(NSDate *)dateValue{
    NSString *currentDate=[Utility dateToStringFormat:[NSDate date] timeZone:UTC];
    NSString *endDateString=[Utility dateToStringFormat:dateValue timeZone:LOCAL];
     if (![currentDate isEqualToString:endDateString]) {
         return TRUE;
     }
     else return FALSE;
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidAppear:animated];
    //picSelectedDate=[NSDate date];
    [self updateTime];
        [[SellerHistoryHandler sharedSellerHistoryHandler]updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] serviceCallBack:^(NSError *error, ItemDetails *response) {
            if (!error) {
                self.itemDetails=response;
            }
        }];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    
    NSString *string =kTime;
    
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 4;
}

- (NSString *)displayTime:(NSDate *)dateToConvert{
    AvialbleTimeTableCell *cell=[[AvialbleTimeTableCell alloc]init];
    return [NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:dateToConvert  timeZone:LOCAL],[cell amPmConvertFromDate:dateToConvert]];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= kDateSelectCell;
    static NSString *simpleTableIdentifier1= kTimeSelectCell;
    static NSString *simpleTableIdentifier3= kTimeSelectCell1;
    static NSString *simpleTableIdentifier2= kAcceptOrdersCell;
    UITableViewCell *cell;
    if (indexPath.row==0) {
        cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        timeLable=(UILabel *)[cell viewWithTag:111];
        timeLable.text=[self makeDisplayStringFromTime:picSelectedDate];
        cell.selectionStyle =UITableViewCellSelectionStyleGray;
    }
    else if (indexPath.row==1){
        availCell=(AvialbleTimeTableCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier1];
        [availCell setStartTime:startDate];
        cell=availCell;
    }
    else if (indexPath.row==2){
        availCell=(AvialbleTimeTableCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier3];
        [availCell setEndTime:endDate];
        cell=availCell;
    }
    else {
        accepCell=(AcceptOrdersTableCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier2];
        [accepCell setAcceptOrdersTime:closeDate];
        cell=accepCell;
    }
    
    
    cell.selectionStyle =UITableViewCellSelectionStyleGray;
    
    return cell;
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    // This will create a "invisible" footer
    return 0.01f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
//    if (indexPath.row==0) {
//        return 103;
//    }else if (indexPath.row==1) {
//        return 163;
//    }else
//        return 118;
    return 98;
}
- (NSString *)amPmConvertFromDate:(NSDate *)date{
    if (date==nil) {
        date=[NSDate date];
    }
    NSMutableString *amPmSrt=[NSMutableString stringWithString:[[Utility dateToStringFormat:kAMPM dateString:date timeZone:UTC] lowercaseString] ];
    [amPmSrt insertString:kDot_String atIndex:1];
    return [NSString stringWithFormat:kAppendWithDot_String,amPmSrt];
}
-(NSDate *)mergeDateValue:(NSDate *)dateValue timeValue:(NSDate *)timeValue{
    NSTimeInterval time = floor([timeValue timeIntervalSinceReferenceDate] / 60.0) * 60.0;
    timeValue = [NSDate dateWithTimeIntervalSinceReferenceDate:time];
    
    NSString *dateValueString=[Utility dateToStringFormat:kDateMonthYearOnly dateString:dateValue timeZone:UTC];
    NSString *dateTimeValueString=[Utility dateToStringFormat:kDateMonthYearOnly dateString:timeValue timeZone:UTC];
    NSString *timevalueString=[Utility dateToStringFormat:kTimeMinSecWithColon dateString:timeValue timeZone:UTC];
    if ([dateValueString isEqualToString:dateTimeValueString]) {
        return timeValue;
    }else{
     NSString *mergedTime=[NSString stringWithFormat:k2StringAppendFormartWithSpace,dateValueString,timevalueString];
    return [Utility stringToDateFormat:kDateAndTimeFormat dateString:mergedTime  timeZone:UTC];
    }
}
-(void)updateTime{
    startDate=[self mergeDateValue:picSelectedDate timeValue:startDate];
    endDate=[self mergeDateValue:picSelectedDate timeValue:endDate];
    closeDate=[self mergeDateValue:picSelectedDate timeValue:closeDate];
    NSLog(@"\n\n\nStart Date==> %@ \n EndDate ==>%@ \n CloseDate ==> %@\n\n\n",[startDate descriptionWithLocale:[NSLocale currentLocale]],[endDate descriptionWithLocale:[NSLocale currentLocale]],[closeDate descriptionWithLocale:[NSLocale currentLocale]]);
    
    [self updatedItems];
}
-(NSDate *)calculatePickerMinimumDate{
    if ([Utility validateToday:picSelectedDate]) {
        return [self getNearestTimeValue];
    }
    return nil;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        [DateAndTimeController selectDateAction:PickerSelectDate withPresentViewController:self completionAction:^(NSDate *selectedDate) {
            picSelectedDate=selectedDate;
            [self updateTime];
            timeLable.text=[self makeDisplayStringFromTime:selectedDate];
            [self updatedItems];
        }];
    }else if(indexPath.row==1){
       [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AvialbleTimeTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
           // NSDate *mergedDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
           //  if (![self validateNextDate:mergedDate]) {
                startDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
                selectedDate=[Utility getLocalTimeValue:selectedDate];
                availableCell.fromLable.text=[NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
                [self updatedItems];
            // }
        }withMinimumDate:[self calculatePickerMinimumDate] currentDate:[self getNearestTimeValueWithTime:picSelectedDate]];
    }
    else if(indexPath.row==2){
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AvialbleTimeTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
          //  NSDate *mergedDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
          //  if (![self validateNextDate:mergedDate]) {
            endDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
            selectedDate=[Utility getLocalTimeValue:selectedDate];
            availableCell.toLable.text=[NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
            [self updatedItems];
          //   }
        }withMinimumDate:startDate currentDate:[self getNearestTimeValueWithTime:picSelectedDate]];
    }
    else if(indexPath.row==3){
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AcceptOrdersTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
           // NSDate *mergedDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
           // if (![self validateNextDate:mergedDate]) {
            closeDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
            selectedDate=[Utility getLocalTimeValue:selectedDate];
            availableCell.availableFrom.text=[NSString stringWithFormat:k2StringAppendFormart,[Utility dateToStringFormat:kTimeMinOnly dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
            [self updatedItems];
           // }
        }withMinimumDate:startDate withMaximumDate:endDate currentDate:[self getNearestTimeValueWithTime:picSelectedDate]];
    }
    
    
}

- (NSString *)makeDisplayStringFromTime:(NSDate *)dateValue{
    
    if ([Utility validateToday:dateValue]) {
        return [NSString stringWithFormat:kTodayWithDate,[Utility dateToStringFormat:dateValue  timeZone:LOCAL] ];
    }
    return [Utility dateToStringFormat:dateValue  timeZone:LOCAL];
}


- (void)updatedItems{
     [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setStartDate:[Utility dateToStringFormat:kDateAndTimeFormat dateString:startDate  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setEndDate:[Utility dateToStringFormat:kDateAndTimeFormat dateString:endDate  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setCloseDate:[Utility dateToStringFormat:kDateAndTimeFormat dateString:closeDate  timeZone:UTC]];
}

- (NSDate *)getNearestTimeValue{
    NSDate *mydate=[NSDate date];
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    return mydate;
}
- (NSDate *)getNearestTimeValueWithTime:(NSDate *)dateValue{
    NSDate *mydate=dateValue;
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    
    return mydate;
}

@end

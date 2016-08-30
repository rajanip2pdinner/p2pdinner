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
    startDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.startDate  timeZone:LOCAL];
    endDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.endDate  timeZone:LOCAL];
    closeDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.closeDate  timeZone:LOCAL];
    picSelectedDate=startDate;
    if (!_isBuyerFlow) {
        //[self addSellerFlowTimeDetails];
    }
    self.title=@"Dinner Listing";
    
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidAppear:animated];

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
    [label setFont:[UIFont fontWithName:@"Plantin" size:18]];
    
    NSString *string =@"Time";
    
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
    return [NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:dateToConvert  timeZone:LOCAL],[cell amPmConvertFromDate:dateToConvert]];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= @"DateSelectCell";
    static NSString *simpleTableIdentifier1= @"TimeSelectCell";
    static NSString *simpleTableIdentifier3= @"TimeSelectCell1";
    static NSString *simpleTableIdentifier2= @"AcceptOrdersCell";
    UITableViewCell *cell;
    if (indexPath.row==0) {
        cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        timeLable=(UILabel *)[cell viewWithTag:111];
        timeLable.text=[self makeDisplayStringFromTime:startDate];
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
    return 78;
}
- (NSString *)amPmConvertFromDate:(NSDate *)date{
    if (date==nil) {
        date=[NSDate date];
    }
    NSMutableString *amPmSrt=[NSMutableString stringWithString:[[Utility dateToStringFormat:@"a" dateString:date timeZone:LOCAL] lowercaseString] ];
    [amPmSrt insertString:@"." atIndex:1];
    return [NSString stringWithFormat:@" %@.",amPmSrt];
}
-(NSDate *)mergeDateValue:(NSDate *)dateValue timeValue:(NSDate *)timeValue{
    NSString *dateValueString=[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:dateValue timeZone:UTC];
    NSString *timevalueString=[Utility dateToStringFormat:@"HH:mm:ss" dateString:timeValue timeZone:UTC];
    NSString *mergedTime=[NSString stringWithFormat:@"%@ %@",dateValueString,timevalueString];
    return [Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:mergedTime  timeZone:UTC];
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        [DateAndTimeController selectDateAction:PickerSelectDate withPresentViewController:self completionAction:^(NSDate *selectedDate) {
            picSelectedDate=selectedDate;
            startDate=[self mergeDateValue:picSelectedDate timeValue:[Utility getLocalTimeValue:startDate]];
            endDate=[self mergeDateValue:picSelectedDate timeValue:[Utility getLocalTimeValue:endDate]];
            closeDate=[self mergeDateValue:picSelectedDate timeValue:[Utility getLocalTimeValue:closeDate]];
            timeLable.text=[self makeDisplayStringFromTime:selectedDate];
            [self updatedItems];
        }];
    }else if(indexPath.row==1){
       [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AvialbleTimeTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
            startDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
            selectedDate=[Utility getLocalTimeValue:selectedDate];
            availableCell.fromLable.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
            [self updatedItems];
        }];
    }
    else if(indexPath.row==2){
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AvialbleTimeTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
            endDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
            selectedDate=[Utility getLocalTimeValue:selectedDate];
            availableCell.toLable.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
            [self updatedItems];
        }];
    }
    else if(indexPath.row==3){
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        AcceptOrdersTableCell *availableCell=[tableView cellForRowAtIndexPath:indexPath];
        [DateAndTimeController selectDateAction:PickerSelectTime withPresentViewController:self completionAction:^(NSDate *selectedDate) {
            closeDate=[self mergeDateValue:picSelectedDate timeValue:selectedDate];
            selectedDate=[Utility getLocalTimeValue:selectedDate];
            availableCell.availableFrom.text=[NSString stringWithFormat:@"%@%@",[Utility dateToStringFormat:@"h.mm" dateString:selectedDate timeZone:UTC],[self amPmConvertFromDate:selectedDate]];
            [self updatedItems];
        }];
    }
    
    
}

- (NSString *)makeDisplayStringFromTime:(NSDate *)dateValue{
    
    if ([Utility validateToday:dateValue]) {
        return [NSString stringWithFormat:@"Today(%@)",[Utility dateToStringFormat:dateValue  timeZone:LOCAL] ];
    }
    return [Utility dateToStringFormat:dateValue  timeZone:LOCAL];
}


- (void)updatedItems{
     [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setStartDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:startDate  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setEndDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:endDate  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setCloseDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:closeDate  timeZone:UTC]];
}

- (NSDate *)getNearestTimeValue{
    NSDate *mydate=[NSDate date];
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    
    return mydate;
}

- (void)addSellerFlowTimeDetails{
    startDate=[[self getNearestTimeValue] dateByAddingTimeInterval:60*60];
    endDate=[startDate dateByAddingTimeInterval:BetweenStartTimeAndEndTime * 60*60];
    closeDate=startDate;
    
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setStartDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:startDate  timeZone:LOCAL]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setEndDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:endDate  timeZone:LOCAL]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setCloseDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:closeDate  timeZone:LOCAL]];
    
}
@end

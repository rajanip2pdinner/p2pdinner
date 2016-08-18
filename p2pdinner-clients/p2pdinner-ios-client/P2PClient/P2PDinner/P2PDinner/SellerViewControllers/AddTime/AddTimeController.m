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
    picSelectedDate=[NSDate date];
    if (!_isBuyerFlow) {
        //[self addSellerFlowTimeDetails];
    }
    self.title=@"Dinner Listing";
    
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self updatedItems];

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
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, tableView.frame.size.width, 18)];
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
    return 3;
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
    static NSString *simpleTableIdentifier2= @"AcceptOrdersCell";
    UITableViewCell *cell;
    if (indexPath.row==0) {
        cell=(UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        timeLable=(UILabel *)[cell viewWithTag:111];
        timeLable.text=[self makeDisplayStringFromTime:startDate];
        
    }
    else if (indexPath.row==1){
        availCell=(AvialbleTimeTableCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier1];
        availCell.delegate=self;
        [availCell setTimeFrom:startDate toTime:endDate];
        availCell.selectionStyle =UITableViewCellSelectionStyleNone;
        return availCell;
    }
    else {
        accepCell=(AcceptOrdersTableCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier2];
        accepCell.delegate=self;
        [accepCell setAcceptOrdersTime:endDate];
        
        accepCell.selectionStyle =UITableViewCellSelectionStyleNone;
        return accepCell;
    }
    
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 103;
    }else if (indexPath.row==1) {
        return 163;
    }else
        return 118;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        DatePicker *datePicker=(DatePicker *)[[UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"DatePickerViewController"];
        datePicker.delegate=self;
        datePicker.selectedDate=startDate;
        [self.navigationController pushViewController:datePicker animated:YES];
    }
}

- (NSString *)makeDisplayStringFromTime:(NSDate *)dateValue{
    
    if ([Utility validateToday:dateValue]) {
        return [NSString stringWithFormat:@"Today(%@)",[Utility dateToStringFormat:dateValue  timeZone:LOCAL] ];
    }
    return [Utility dateToStringFormat:dateValue  timeZone:LOCAL];
}

- (void)selectedDate:(NSDate *)selectedDateValue{
    
    if(addTimeTableview.frame.origin.y!=0)
    {
        [addTimeTableview setFrame:CGRectMake(addTimeTableview.frame.origin.x, 0, addTimeTableview.frame.size.width, addTimeTableview.frame.size.height)];
    }
    picSelectedDate=selectedDateValue;
    timeLable.text=[self makeDisplayStringFromTime:selectedDateValue];
}

- (void)updatedItems{
    //  NSLog(@"%@",timeLable.text);
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setStartDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[availCell getStartTime:picSelectedDate]  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setEndDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[availCell getStopTime:picSelectedDate]  timeZone:UTC]];
    [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setCloseDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[accepCell getAcceptOrdersTime:picSelectedDate]  timeZone:UTC]];
    
    // NSLog(@"%@ ,%@ %@",[[availCell getStartTime:picSelectedDate] descriptionWithLocale:[NSLocale systemLocale]] ,[[availCell getStopTime:picSelectedDate]descriptionWithLocale:[NSLocale systemLocale]],[[accepCell getAcceptOrdersTime:picSelectedDate]descriptionWithLocale:[NSLocale systemLocale]]);
    
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

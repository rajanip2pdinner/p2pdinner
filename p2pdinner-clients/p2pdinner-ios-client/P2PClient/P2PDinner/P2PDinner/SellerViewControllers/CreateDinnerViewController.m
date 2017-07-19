//
//  CreateDinnerViewController.m
//  P2PDinner
//
//  Created by sudheerkumar on 2/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CreateDinnerViewController.h"
#import "SellerHistoryHandler.h"
#import "SharedLogin.h"
#import "Utility.h"
#import "AddFoodPhotos.h"
#import "StringConstants.h"

@interface CreateDinnerViewController ()
{
    __block NSArray *oldAddedArray;
    UINavigationController *navController;
}

@end

@implementation CreateDinnerViewController

+ (void)backButtonAction{
    
}
//Short arry without Case sensitive
- (NSArray *)getSortedArray:(NSArray *)responceArray{
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"title" ascending:YES comparator:^NSComparisonResult(id obj1, id obj2) {
        NSStringCompareOptions comparisonOptions = NSCaseInsensitiveSearch | NSNumericSearch | NSWidthInsensitiveSearch | NSForcedOrderingSearch;
        NSRange string1Range = NSMakeRange(0, ((NSString *)obj1).length);
        return [(NSString *)obj1 compare: (NSString *)obj2 options: comparisonOptions range: string1Range locale: [NSLocale currentLocale]];
    }];
    return [responceArray sortedArrayUsingDescriptors:@[sortDescriptor]];
}

- (void)loadUserDinnerHistory{
    if ([[SharedLogin sharedLogin] userId]) {
        ItemDetails *dummyItem=[[ItemDetails alloc]init];
        
        [dummyItem setTitle:@"Loading..."];
        oldAddedArray=[NSArray arrayWithObject:dummyItem];
        [tableView reloadData];
        SellerHistoryHandler *obj=[SellerHistoryHandler sharedSellerHistoryHandler];
        [obj getUserHistory:[[[SharedLogin sharedLogin] userId] stringValue] serviceCallBack:^(NSError *error, NSArray *response)
         {
             oldAddedArray=[self getSortedArray:response];
             [tableView reloadData];
             
         }];
    }
}

- (void)valdateOldDinnerArray{
    NSPredicate *dinnerIdCheck = [NSPredicate predicateWithFormat:@"(dinnerId = 0)AND(title = '')"];
    NSArray *needTodRemoveArray=[oldAddedArray filteredArrayUsingPredicate:dinnerIdCheck];
    NSMutableArray *arrayWithDustObjects=[NSMutableArray arrayWithArray:oldAddedArray];
    if ([needTodRemoveArray count]>0) {
        for (int i=0; i<[needTodRemoveArray count]; i++) {
            [arrayWithDustObjects removeObject:[needTodRemoveArray objectAtIndex:i]];
        }
        oldAddedArray=[NSArray arrayWithArray:arrayWithDustObjects];
        [tableView reloadData];
    }
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [tableView reloadData];
    [self valdateOldDinnerArray];
}
- (id)validateNull:(id)object{
    if (!(object == (id)[NSNull null]) || [object isKindOfClass:[NSString class]]) {
        return object;
    }
    return @"";
}
- (void)viewDidLoad {
    //[[SharedLogin sharedLogin] setUserId:[NSNumber numberWithInt:935]];    [[NSUserDefaults standardUserDefaults]setObject:@"935" forKey:@"userId"];
    NSString *userCert = [[SharedLogin sharedLogin] userCertificates];
    userCert = [self validateNull:userCert];
    if (!userCert || userCert.length <= 3) {
        //Need to show food safty
        
        AddFoodPhotos *addFoodPhotos = [self.storyboard instantiateViewControllerWithIdentifier:@"addFoodPhotosViewController"];
        ItemDetails *itemDetails=[[ItemDetails alloc]init];
        //itemDetails.imageUri = userCert;
        [addFoodPhotos setTitle:kSafetyProfile];
        [addFoodPhotos setItemDetails:itemDetails];
        [addFoodPhotos setIsFromFoodSafty:YES];
        UINavigationController *nv = [[UINavigationController alloc] initWithRootViewController:addFoodPhotos];
        [addFoodPhotos completionAction:^(bool value) {
            if (value) {
                [self.navigationController popViewControllerAnimated:YES];
                
            }
        }];
        [self presentViewController:nv animated:YES completion:^{
            
        }];
    }
    
    
    [self loadUserDinnerHistory];
    UIBarButtonItem *newBackButton = [[UIBarButtonItem alloc] initWithTitle:@"Home" style:UIBarButtonItemStylePlain target:self action:@selector(backButtonAction)];
    self.navigationItem.leftBarButtonItem=newBackButton;
    [super viewDidLoad];
    tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 2+[oldAddedArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView1 cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell ;
    if(indexPath.row==0){
        cell= [tableView1 dequeueReusableCellWithIdentifier:@"CreateNewCell"];
    }
    else if(indexPath.row==1)
    {
        cell= [tableView1 dequeueReusableCellWithIdentifier:@"SelectFromFavoritCell"];
        
    }
    else
    {
        cell= [tableView1 dequeueReusableCellWithIdentifier:@"LatestItemCell"];
        UILabel *nameTitleValue=(UILabel *)[cell viewWithTag:101];
        UILabel *description=(UILabel *)[cell viewWithTag:102];
        //NSLog(@"test %ld",(long)indexPath.row);
        ItemDetails *item=[oldAddedArray objectAtIndex:(indexPath.row-2)];
        nameTitleValue.text=item.title;
        description.text=item.dinnerDescription;
        
    }
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 57;
    }
    else if (indexPath.row==1) {
        return 72;
    }
    
    return 57;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row!=1) {
        UIStoryboard *mystoryboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        MenuViewController *menuViewController = [mystoryboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
        [self navigateMenuView:menuViewController indexPath:indexPath];
    }
}
//method used to add date first time
-(NSDate *)validateEndAndCloseTime:(NSDate *)inputDate{
        NSDate *calculatedDate;
        NSDate *mydate = [NSDate date];
        NSTimeInterval nextHourInterveral = 3 * 60 * 60;
    calculatedDate=[Utility getNearestTimeValueWithTime:[mydate dateByAddingTimeInterval:nextHourInterveral]];

    NSString *currentDate=[Utility dateToStringFormat:[NSDate date] timeZone:UTC];
    NSString *endDateString=[Utility dateToStringFormat:calculatedDate timeZone:LOCAL];
    
    if (![currentDate isEqualToString:endDateString]) {
        NSString *calculatedDateString=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility endOfDay:[NSDate date]] timeZone:UTC];
        return [Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:calculatedDateString  timeZone:UTC];
    }
    
    return calculatedDate;
 }
-(NSDate *)calculateEndAndCloseTimeOfExistingItem:(NSDate *)inputDate{
    
    NSDate *calculatedDate=[Utility getLocalTimeValue:inputDate];
    
    if ([calculatedDate timeIntervalSinceNow] < 0.0){
    NSDate *mydate = [NSDate date];
    NSTimeInterval nextHourInterveral = 3 * 60 * 60;
    calculatedDate=[Utility getNearestTimeValueWithTime:[mydate dateByAddingTimeInterval:nextHourInterveral]];
    }
    NSString *currentDate=[Utility dateToStringFormat:[NSDate date] timeZone:UTC];
    NSString *endDateString=[Utility dateToStringFormat:calculatedDate timeZone:LOCAL];
    
    if (![currentDate isEqualToString:endDateString]) {
        NSString *calculatedDateString=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[Utility endOfDay:[NSDate date]] timeZone:UTC];
        return [Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:calculatedDateString  timeZone:LOCAL];
    }
    
    return inputDate;
}

- (NSString *)createDefaultDate:(DinnerTime)dinnertime{
    NSString *returnString;
    
    switch (dinnertime) {
        case startTime:
        {
            returnString=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[self getNearestTimeValueWithTime:[NSDate date]] timeZone:LOCAL];
        }
            break;
        case endTime:
        {
            returnString=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[self validateEndAndCloseTime:[self getNearestTimeValueWithTime:[NSDate date]]] timeZone:LOCAL];
        }
            break;
        case acceptOrderTime:
        {
             returnString=[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:[self validateEndAndCloseTime:[self getNearestTimeValueWithTime:[NSDate date]]] timeZone:LOCAL];
        }
            break;
    }
    
    
    return returnString;
}
- (NSDate *)getNearestTimeValueWithTime:(NSDate *)dateValue{
    NSDate *mydate=dateValue;
    NSDateComponents *time = [[NSCalendar currentCalendar]components: NSCalendarUnitHour |NSCalendarUnitMinute fromDate: mydate];
    NSUInteger remainder = ([time minute] % 15);
    mydate = [mydate dateByAddingTimeInterval: 60 * (15 - remainder)];
    
    return mydate;
}
-(ItemDetails *)createNewItem{
     ItemDetails *item;
    item=[[ItemDetails alloc]init];
    [item setTitle:@""];
    [item setDinnerDescription:@""];
    NSString *userId=[[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    [item setUserId:[NSNumber numberWithDouble:[userId doubleValue]]];
    
    [item setDinnerCategories:@""];
    [item setDinnerSpecialNeeds:@""];
    [item setDinnerDelivery:@"To-Go"];
    [item setImageUri:@""];
    [item setIsActive:YES];
    [item setEndDate:[self createDefaultDate:endTime]];
    [item setStartDate:[self createDefaultDate:startTime]];
    [item setCloseDate:[self createDefaultDate:acceptOrderTime]];
    [item setAddressLine1:@""];
    [item setAddressLine2:@""];
    [item setCity:@""];
    [item setState:@""];
    [item setZipCode:@""];
    [item setAvailableQuantity:[NSNumber numberWithInt:5]];
    [item setCostPerItem:[NSNumber numberWithInt:1]];
    [item setDinnerId:[NSNumber numberWithInt:0]];
    return item;
}
-(ItemDetails *)getUpdatedCurrentDate:(ItemDetails *)itemDetails{
    NSDate *startDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.startDate  timeZone:UTC];
    NSDate *endDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.endDate  timeZone:UTC];
    NSDate *closeDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:itemDetails.closeDate  timeZone:UTC];
    startDate=[Utility mergeDateValue:[NSDate date] timeValue:[Utility getLocalTimeValue:startDate]];
    endDate=[Utility mergeDateValue:[NSDate date] timeValue:[Utility getLocalTimeValue:endDate]];
    closeDate=[Utility mergeDateValue:[NSDate date] timeValue:[Utility getLocalTimeValue:closeDate]];
    //endDate=[self calculateEndAndCloseTimeOfExistingItem:endDate];
    //closeDate=[self calculateEndAndCloseTimeOfExistingItem:closeDate];
    [itemDetails setStartDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:startDate  timeZone:UTC]];
    [itemDetails setEndDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:endDate  timeZone:UTC]];
    [itemDetails setCloseDate:[Utility dateToStringFormat:@"MM/dd/yyyy HH:mm:ss" dateString:closeDate  timeZone:UTC]];
    
    return itemDetails;
}
- (void)navigateMenuView:(MenuViewController *)viewController indexPath:(NSIndexPath *)path{
 //UINavigationController *navigationController =[[UINavigationController alloc] initWithRootViewController:viewController];
        if (path.row==0) {
            ItemDetails *item=[self createNewItem];
            NSMutableArray *oldArray=[NSMutableArray arrayWithArray:oldAddedArray];
            [oldArray addObject:item];
            oldAddedArray=[NSArray arrayWithArray:oldArray];
            [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:item];
            [viewController setItemDetails:item];
            
        }
        else
        {
            ItemDetails *selectedItem=[oldAddedArray objectAtIndex:(path.row-2)];
            [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:[self getUpdatedCurrentDate:selectedItem]];
            [viewController setItemDetails:[oldAddedArray objectAtIndex:(path.row-2)]];
        }
        //[self naviagtionBarUISetup];
        [self presentViewController:viewController animated:YES completion:^{
            
        }];
}
- (void)cancelAction{
    //[self updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (void)naviagtionBarUISetup{
    navController.navigationBar.translucent = false;
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:@"Plantin" size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    [navController.navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [navController.navigationBar setBarTintColor:navBarColor];
    [navController.navigationItem.leftBarButtonItem setTintColor:[UIColor whiteColor]];
}
- (void)setUpNavigationBar{
    [self naviagtionBarUISetup];
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setTitle:@"Cancel" forState:UIControlStateNormal];
    button.frame=CGRectMake(0.0, 100.0, 60.0, 30.0);
    [button addTarget:self action:@selector(cancelAction)  forControlEvents:UIControlEventTouchUpInside];
    navController.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    
}

- (void)UpdateDinnerItems{
    [self loadUserDinnerHistory];
}
/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end

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

@interface CreateDinnerViewController ()
{
    __block NSArray *oldAddedArray;
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

- (void)viewDidLoad {
    //[[SharedLogin sharedLogin] setUserId:[NSNumber numberWithInt:935]];    [[NSUserDefaults standardUserDefaults]setObject:@"935" forKey:@"userId"];
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
        [self performSegueWithIdentifier:@"CreateNew" sender:nil];
    }
}

- (NSString *)createDefaultDate:(DinnerTime)dinnertime{
    NSString *returnString;
    
    switch (dinnertime) {
        case startTime:
        {
            returnString=[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:[NSDate date] timeZone:LOCAL];
            returnString= [NSString stringWithFormat:@"%@ 19:00:00",returnString];
        }
            break;
        case endTime:
        {
            returnString=[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:[NSDate date]  timeZone:LOCAL];
            returnString= [NSString stringWithFormat:@"%@ 22:00:00",returnString];
        }
            break;
        case acceptOrderTime:
        {
            returnString=[Utility dateToStringFormat:@"MM/dd/yyyy" dateString:[NSDate date]  timeZone:LOCAL];
            returnString= [NSString stringWithFormat:@"%@ 16:00:00",returnString];
        }
            break;
    }
    
    
    return returnString;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSIndexPath *path = [tableView indexPathForSelectedRow];
    ItemDetails *item;
    if (path.row==0) {
        item=[[ItemDetails alloc]init];
        [item setTitle:@""];
        [item setDinnerDescription:@""];
        NSString *userId=[[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
        [item setUserId:[NSNumber numberWithDouble:[userId doubleValue]]];
        
        [item setDinnerCategories:@""];
        [item setDinnerSpecialNeeds:@""];
        [item setDinnerDelivery:@""];
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
        NSMutableArray *oldArray=[NSMutableArray arrayWithArray:oldAddedArray];
        [oldArray addObject:item];
        oldAddedArray=[NSArray arrayWithArray:oldArray];
        UINavigationController *navController=[segue destinationViewController];
        MenuViewController *viewController=(MenuViewController *)[navController topViewController];
        [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:item];
        [viewController setItemDetails:item];
        
    }
    else
    {
        //item=[oldAddedArray objectAtIndex:(path.row-2)];
        
        UINavigationController *navController=[segue destinationViewController];
        MenuViewController *viewController=(MenuViewController *)[navController topViewController];
        [[ItemDetailsShared sharedItemDetails] setSharedItemDetailsValue:[oldAddedArray objectAtIndex:(path.row-2)]];
        [viewController setItemDetails:[oldAddedArray objectAtIndex:(path.row-2)]];
    }
    
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

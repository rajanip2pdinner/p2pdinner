//
//  DinnerResultViewController.m
//  P2PDinner
//
//  Created by Selvam M on 8/27/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DinnerResultViewController.h"
#import "ItemDetails.h"
#import "SellerHistoryHandler.h"
#import "Utility.h"
#import "MenuViewController.h"
#import "AFImageRequestOperation.h"
#import "SWTableViewCell.h"
#import "ItemDetailViewController.h"

@interface DinnerResultViewController ()<SWTableViewCellDelegate>{
    ActivityView *activityView;
}

@end

@implementation DinnerResultViewController

- (void)viewDidLoad {
    activityView=[[ActivityView alloc]initWithFrame:self.view.frame];
    [super viewDidLoad];
    self.title=@"I Want Dinner";
    [self getDinnerCurrentListing];
    
    self.dinnerResultTable.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    // Do any additional setup after loading the view.
    
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
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [_dinnerListArray count];
}
- (NSString *)getTimeFromDate:(NSString *)inputDateString{
    NSDate *inputDate=[Utility stringToDateFormat:@"MM/dd/yyyy HH:mm:ss" dateString:inputDateString  timeZone:UTC];
    
    return [Utility dateToStringFormat:@"h.mm a" dateString:inputDate  timeZone:LOCAL];
}
- (NSString *)makeImageURLfromimageName:(NSString *)imageNames
{
    if ((NSString *)[NSNull null] !=imageNames) {
        NSString *imageName;
        NSArray *mutableImageURL=[imageNames componentsSeparatedByString:@","];
        if ([mutableImageURL count]>0) {
            imageName=[mutableImageURL objectAtIndex:0];
            imageName=[imageName stringByReplacingOccurrencesOfString:@"\"" withString:@""];
        }
        return imageName;
    }
    return @"";
    
}
- (void)imageRequestOperation:(NSString *)photoUrl witImagView:(UIImageView *)imageView
{
    
    // download the photo
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:photoUrl]];
    [request setAccessibilityLabel:@"selvam"];
    AFImageRequestOperation *operation = [AFImageRequestOperation imageRequestOperationWithRequest:request imageProcessingBlock:^UIImage *(UIImage *image) {
        return image;
    } success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
        imageView.image=image;
        
    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
        
    }];
    [operation start];
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 120.0f;
}
- (NSArray *)rightButtons
{
    NSMutableArray *rightUtilityButtons = [NSMutableArray new];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:0.78f green:0.78f blue:0.8f alpha:1.0]
                                                title:@"Buy 1"];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
                                                title:@"Cancel"];
    
    return rightUtilityButtons;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= @"DinnerCell";
    SWTableViewCell *cell;
    if (cell == nil) {
        cell=(SWTableViewCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        cell.rightUtilityButtons = [self rightButtons];
        cell.delegate = self;
    }
#warning Need to cleanup code
    UIImageView *dinnerImage=(UIImageView *)[cell viewWithTag:100];
    dinnerImage.layer.cornerRadius = 48.0f;
    dinnerImage.layer.masksToBounds = YES;
    //dinnerImage.layer.borderWidth = .5f;
    dinnerImage.layer.shadowColor = [UIColor orangeColor].CGColor;
    //dinnerImage.layer.shadowOpacity = 0.4;
    //dinnerImage.layer.shadowRadius = 48.0f;
    
    
    UILabel *dinnerTitle=(UILabel *)[cell viewWithTag:101];
    UILabel *dinnerDistance=(UILabel *)[cell viewWithTag:102];
    UILabel *dinnerCategory=(UILabel *)[cell viewWithTag:103];
    UILabel *dinnerDescription=(UILabel *)[cell viewWithTag:104];
    UILabel *dinnerPrice=(UILabel *)[cell viewWithTag:105];
    UILabel *dinnerDelivery=(UILabel *)[cell viewWithTag:106];
    UILabel *dinnerServingDuration=(UILabel *)[cell viewWithTag:107];
    ResultItemDetails *dinnerObj=[_dinnerListArray objectAtIndex:indexPath.row];
    dinnerTitle.text=dinnerObj.title;
    if ((NSString *)[NSNull null] != dinnerObj.imageUri) {
         [self imageRequestOperation:[self makeImageURLfromimageName:dinnerObj.imageUri] witImagView:dinnerImage];
    }
    else{
        [dinnerImage setImage:[UIImage imageNamed:@"noImage"]];
    }
    dinnerTitle.text=dinnerObj.title;
    dinnerDistance.text=dinnerObj.distance;
    dinnerCategory.text=dinnerObj.dinnerCategories;
    dinnerDescription.text=dinnerObj.dinnerDescription;
    dinnerPrice.text=[NSString stringWithFormat:@"$ %.2f",[dinnerObj.costPerItem floatValue]];    
    dinnerDelivery.text=dinnerObj.dinnerDelivery;
    [self getAttributedFontForLable:dinnerDelivery];
    dinnerServingDuration.text=[NSString stringWithFormat:@"%@-%@",[self getTimeFromDate:dinnerObj.startDate],[self getTimeFromDate:dinnerObj.endDate]];

    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    return cell;
    
    
}
-(NSAttributedString *)getStringBGColor:(NSString *)string{
    NSRange range = NSMakeRange(0,[string length]);
    NSMutableAttributedString *attributedString=[[NSMutableAttributedString alloc]initWithString:string];
    [attributedString addAttribute:NSBackgroundColorAttributeName value:[UIColor orangeColor] range:range];
    NSRange spaceRange = [string rangeOfString:@","];
    [attributedString addAttribute:NSBackgroundColorAttributeName value:[UIColor whiteColor] range:spaceRange];
    return attributedString;
}
-(void)getAttributedFontForLable:(UILabel* )deliveryLable{
    [deliveryLable setAttributedText:[self getStringBGColor:deliveryLable.text]];
}
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    ItemDetailViewController *nvViewController=[segue destinationViewController];
    [nvViewController setItemDetails:sender];
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    //7845487699
    ResultItemDetails *dinnerObj=[_dinnerListArray objectAtIndex:indexPath.row];
    [self performSegueWithIdentifier:@"ItemDetailViewController" sender:dinnerObj];
    
}
- (void)getDinnerCurrentListing{
  
}
- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerRightUtilityButtonWithIndex:(NSInteger)index{
    NSLog(@"Need to Implement cart");
    [cell hideUtilityButtonsAnimated:YES];
}

@end

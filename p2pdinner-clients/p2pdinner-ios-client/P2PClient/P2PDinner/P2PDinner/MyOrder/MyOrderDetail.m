//
//  MyOrderDetail.m
//  P2PDinner
//
//  Created by Selvam M on 7/30/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MyOrderDetail.h"
#import "SellerHistoryHandler.h"
#import "MyOrderItem.h"
#import "StarRatingView.h"
#import "BuyerHandler.h"
@interface MyOrderDetail()<RatingDelegate>
@end
@implementation MyOrderDetail
- (void)viewDidLoad{
    [super viewDidLoad];
    self.title=@"My Orders";
    self.myOrderDetailTable.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    [self getOrderDetails];
}
#pragma myOrderDetail
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    //tableView.sectionHeaderHeight=100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 38)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:@"Plantin" size:18]];
    
    NSString *string =_selectedMenu;
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor whiteColor]]; //your background color...
    return view;
}
- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(nonnull NSIndexPath *)indexPath{
    return 103;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(nonnull NSIndexPath *)indexPath{
    return UITableViewAutomaticDimension;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 38.0;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return [_myOrderArray count];
}
- (UITableViewCell *)tableView:(UITableView *)myOrderDetailtableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    
    if (cell==nil) {
        if (([[_myOrderArray objectAtIndex:indexPath.row] isKindOfClass:[CarRecivedItemDetail class]])) {
            cell=[myOrderDetailtableView dequeueReusableCellWithIdentifier:@"myOrderDetailCell"];
        }
        else{
            cell=[myOrderDetailtableView dequeueReusableCellWithIdentifier:@"MyOrdersDetailCellLoading"];
        }
    }
    
    //CarRecivedItemDetail
    if ([[_myOrderArray objectAtIndex:indexPath.row] isKindOfClass:[CarRecivedItemDetail class]]) {
        CarRecivedItemDetail *cartRec=[_myOrderArray objectAtIndex:indexPath.row];
        UILabel *BuyerName=(UILabel *)[cell viewWithTag:3];
        id name=[cartRec buyerName];
        BuyerName.text=(name==[NSNull null])?@"":[cartRec buyerName];
        UILabel *password=(UILabel *)[cell viewWithTag:1];
        [password setText:[NSString stringWithFormat:@" %@ ",cartRec.passCode]];
        UILabel *plats=(UILabel *)[cell viewWithTag:2];
        [plats setText:[NSString stringWithFormat:@"%@ Plates",[cartRec.orderQuantity stringValue]]];
        StarRatingView *stareRatingView=(StarRatingView *)[cell viewWithTag:4];
        [stareRatingView setMaxrating:[cartRec.seller_rating intValue]*20];
        stareRatingView.delegate=self;
        [stareRatingView setCartId:[cartRec.cart_id stringValue]];
    }
    
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    return cell;
}
- (void)getOrderDetails{
    _myOrderArray=[NSArray arrayWithObject:@"Loading"];
    [self.myOrderDetailTable reloadData];
    NSString *requestStr=[NSString stringWithFormat:@"%ld/received/detail",(long)[_selectedMenuId integerValue]];
    [[SellerHistoryHandler sharedServiceHandler] getMyOrderReceived:requestStr serviceCallBack:^(NSError *error, NSArray *response) {
        _myOrderArray=response;
        [self.myOrderDetailTable reloadData];
        
    }];
}
-(void)updatedRatingValue:(int)ratingValue withCartId:(NSString *)cartId{
     [self sellerRatingUpdate:[NSString stringWithFormat:@"%d",(ratingValue/20)] withCartId:cartId];
}
-(void)sellerRatingUpdate:(NSString *)sellerRating withCartId:(NSString *)cartId{
    NSString *requstObject=[NSString stringWithFormat:@"{\"seller_rating\": %@}",sellerRating];
    [[BuyerHandler sharedBuyerHandler] addRating:requstObject withCartId:cartId withResponse:nil];
    
}

@end

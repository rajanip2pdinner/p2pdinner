//
//  CostViewController.m
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CostViewController.h"
@interface CostViewController()<CostCellDelegate,MinimumOrdersDelegate>
@end
@implementation CostViewController
@synthesize itemDetails;


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    //tableView.sectionHeaderHeight=100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 10, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:@"Plantin" size:18]];
    
    NSString *string =@"Cost";
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= @"CostCell";
    static NSString *simpleTableIdentifier1= @"MaximumCell";
    UITableViewCell *cell;
    
    if (indexPath.row==0) {
        costCell=(CostCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        costCell.delegate=self;
        [costCell setPrice:itemDetails.costPerItem];
        costCell.selectionStyle =UITableViewCellSelectionStyleNone;
        
        return costCell;
    }
    else if (indexPath.row==1){
        minOrdCell=(MinimumOrdersCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier1];
        minOrdCell.delegate=self;
        [minOrdCell setMaximuOrdersValue:itemDetails.availableQuantity];
        
        minOrdCell.selectionStyle =UITableViewCellSelectionStyleNone;
        return minOrdCell;
    }
    
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0) {
        return 169;
    }
    return 115;
}

-(void)updatedItems{
    itemDetails.costPerItem=[costCell getCostForDinner];
    itemDetails.availableQuantity=[minOrdCell getMaximuOrdersValue];
    //   NSLog(@"\n\n dollorsLable %@ \n centsLable %@ \n ",[costCell getCostForDinner],[minOrdCell getMaximuOrdersValue]);
}
@end
